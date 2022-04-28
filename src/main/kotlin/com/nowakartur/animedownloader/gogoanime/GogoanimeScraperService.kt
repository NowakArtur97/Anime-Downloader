package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.goload.GoloadDownloadPage
import com.nowakartur.animedownloader.m4upload.M4UploadPage
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.SubscribedAnimeService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GogoanimeScraperService(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val goloadDownloadPage: GoloadDownloadPage,
    private val m4UploadPage: M4UploadPage,
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

            logger.info("Connecting to episode page of: ${it.title}.")

            val episodePage = gogoanimeEpisodePage.connectToEpisodePage(linkToAnimePage)

            val goloadLink = gogoanimeEpisodePage.findLinkForDownload(episodePage)

            logger.info("Connecting to download page: $goloadLink.")

            subscribedAnimeService.startDownloadingAnime(it)

            val webDriver = SeleniumUtil.startWebDriver()

            goloadDownloadPage.connectToGolandPage(webDriver, goloadLink)

            try {
                val m4UploadDownloadLink = goloadDownloadPage.findM4UploadDownloadLink(webDriver)!!

                logger.info("Link for ${it.title} on m4Upload: $m4UploadDownloadLink.")

                m4UploadPage.connectToDownloadPage(webDriver, m4UploadDownloadLink)

                logger.info("Download ${it.title} from m4Upload.")

                m4UploadPage.downloadEpisode(webDriver)

                SeleniumUtil.waitForFileDownload(webDriver)

                subscribedAnimeService.finishDownloadingAnime(it)

                logger.info("Anime ${it.title} successfully downloaded.")

            } catch (e: Exception) {
                logger.info("Unexpected exception occurred when downloading ${it.title}.")

                subscribedAnimeService.startDownloadingAnime(it)

                logger.info(e.message)
            } finally {
                webDriver.quit()
            }
        }
    }
}
