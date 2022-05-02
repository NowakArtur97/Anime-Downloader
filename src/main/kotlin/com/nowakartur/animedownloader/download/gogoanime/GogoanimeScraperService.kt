package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.DownloadFacade
import com.nowakartur.animedownloader.download.goload.GoloadDownloadPage
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.openqa.selenium.chrome.ChromeDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GogoanimeScraperService(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val goloadDownloadPage: GoloadDownloadPage,
    private val downloadFacade: DownloadFacade,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadAnime() {

        logger.info("Searching for all subscribed anime for download.")

        val subscribedAnime = subscribedAnimeService.findAllAnimeForDownload()

        if (subscribedAnime.isEmpty()) {
            logger.info("No anime is subscribed.")
            return
        }

        logger.info("Connecting to gogoanime page.")

        val mainPage: Document = gogoanimeMainPage.connectToMainPage()

        val allNewAnimeToDownloadElements: List<Element> =
            gogoanimeMainPage.findAllSubscribedAnime(subscribedAnime, mainPage)

        if (allNewAnimeToDownloadElements.isEmpty()) {
            logger.info("Nothing new to download.")
            return
        }

        val allNewAnimeToDownload: List<SubscribedAnimeEntity> = subscribedAnime
            .filter { anime -> allNewAnimeToDownloadElements.any { it.text().contains(anime.title) } }

        logger.info("Anime found: ${allNewAnimeToDownload.map { it.title }}.")

        allNewAnimeToDownload.forEach {
            val linkToAnimePage: String = gogoanimeMainPage.findLinkToEpisodes(allNewAnimeToDownloadElements, it.title)

            logger.info("Connecting to the episode page of: [${it.title}].")

            val episodePage = gogoanimeEpisodePage.connectToEpisodePage(linkToAnimePage)

            val goloadLink = gogoanimeEpisodePage.findLinkForDownload(episodePage)

            logger.info("Connecting to the download page: [$goloadLink].")

            subscribedAnimeService.startDownloadingAnime(it)

            var webDriver: ChromeDriver? = null

            try {

                webDriver = SeleniumUtil.startWebDriver()

                goloadDownloadPage.connectToGolandPage(webDriver, goloadLink)

                val allDownloadLinks = goloadDownloadPage.findAllDownloadLinks(webDriver)

                downloadFacade.downloadInBestQuality(webDriver, allDownloadLinks)
//                if (link.contains(M4_UPLOAD_TEXT)) {
//                    logger.info("Link to the episode of [${it.title}] on M4Upload: [$link].")
//                    m4UploadPage.connectToDownloadPage(webDriver, link)
//                    logger.info("Downloading an episode of [${it.title}] from M4Upload.")
//                    m4UploadPage.downloadEpisode(webDriver)
//                } else if (link.contains(STREAM_SB_UPLOAD_TEXT)) {
//                    logger.info("Link to the episode of [${it.title}] on StreamSB: [$link].")
//                    streamSbPage.connectToDownloadPage(webDriver, link)
//                    logger.info("Downloading an episode of [${it.title}] from StreamSB.")
//                    streamSbPage.downloadEpisode(webDriver)
//                }

                SeleniumUtil.waitForFileDownload(webDriver)

                subscribedAnimeService.finishDownloadingAnime(it)

                logger.info("The [${it.title}] episode has been successfully downloaded.")

            } catch (e: Exception) {
                logger.info("Unexpected exception occurred when downloading episode of [${it.title}].")

                logger.info(e.message)

                subscribedAnimeService.waitForDownload(it)
            } finally {
                webDriver?.quit()
            }
        }
    }
}
