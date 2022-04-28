package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.goload.GoloadDownloadPage
import com.nowakartur.animedownloader.m4upload.M4UploadPage
import com.nowakartur.animedownloader.subsciption.SubscribedAnimeEntity
import com.nowakartur.animedownloader.util.SeleniumUtil
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GogoanimeScraperService(
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val goloadDownloadPage: GoloadDownloadPage,
    private val m4UploadPage: M4UploadPage,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadAnime(subscribedAnime: List<SubscribedAnimeEntity>) {

        logger.info("Connecting to gogoanime page.")

        val mainPage: Document = gogoanimeMainPage.connectToMainPage()

        val allSubscribedAnime: List<Element> =
            gogoanimeMainPage.findAllSubscribedAnime(subscribedAnime.map { it.title }, mainPage)

        logger.info("Anime found: ${allSubscribedAnime.map { it.text() }}.")

        if (allSubscribedAnime.isNotEmpty()) {

            val allLinksToAnimePages: List<String> =
                gogoanimeMainPage.findAllLinksToEpisodes(allSubscribedAnime, mainPage)

            val webDriver = SeleniumUtil.startWebDriver()

            allLinksToAnimePages.map {

                logger.info("Connecting to episode page: $it.")

                val episodePage = gogoanimeEpisodePage.connectToEpisodePage(it)

                gogoanimeEpisodePage.findLinkForDownload(episodePage)

            }.forEach() { downloadLink ->

                logger.info("Connecting to download page: $downloadLink.")

                goloadDownloadPage.connectToGolandPage(webDriver, downloadLink)

                try {
                    val m4UploadDownloadLink = goloadDownloadPage.findM4UploadDownloadLink(webDriver)!!

                    logger.info("Link for M4Upload: $m4UploadDownloadLink.")

                    m4UploadPage.connectToDownloadPage(webDriver, m4UploadDownloadLink)

                    logger.info("Download file from M4Upload.")

                    m4UploadPage.downloadEpisode(webDriver)

                    SeleniumUtil.waitForFileDownload(webDriver)

                    logger.info("Download completed.")

                } catch (e: Exception) {
                    logger.info("Unexpected exception occurred.")
                    logger.info(e.message)
                } finally {
                    webDriver.quit()
                }
            }
        }
    }
}
