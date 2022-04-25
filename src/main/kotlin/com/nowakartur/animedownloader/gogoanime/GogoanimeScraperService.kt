package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.goland.GolandDownloadPage
import com.nowakartur.animedownloader.subsciption.SubscribedAnimeService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GogoanimeScraperService(
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val golandDownloadPage: GolandDownloadPage,
    private val subscribedAnimeService: SubscribedAnimeService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun scrapDownloadLinks() {

        logger.info("Connecting to gogoanime page.")

        val mainPage: Document = gogoanimeMainPage.connectToMainPage()

        logger.info("Searching for all subscribed anime.")

        val subscribedAnime = subscribedAnimeService.findAllSubscribedAnime()
        val allSubscribedAnime: List<Element> =
            gogoanimeMainPage.findAllSubscribedAnime(subscribedAnime, mainPage)

        logger.info("Anime found: ${allSubscribedAnime.map { it.text() }}.")

        val allLinksToAnimePages: List<String> =
            gogoanimeMainPage.findAllLinksToEpisodes(allSubscribedAnime, mainPage)

        logger.info("Anime links found: $allLinksToAnimePages.")

        val allLinksForDownload = allLinksToAnimePages.map {

            logger.info("Connecting to episode page: $it.")

            val episodePage = gogoanimeEpisodePage.connectToEpisodePage(it)

            gogoanimeEpisodePage.findLinkForDownload(episodePage)
        }

        logger.info("Anime download links found: $allLinksForDownload.")

        allLinksForDownload.forEach() {

            logger.info("Connecting to download page: $it.")

            val downloadPage = golandDownloadPage.connectToGolandPage(it)

            val m4UploadDownloadLink = golandDownloadPage.findM4UploadDownloadLink(downloadPage)

            logger.info("Link for M4Upload: $m4UploadDownloadLink.")
        }
    }
}
