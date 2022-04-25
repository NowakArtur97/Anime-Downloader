package com.nowakartur.animedownloader

import com.nowakartur.animedownloader.gogoanime.GogoanimeEpisodePage
import com.nowakartur.animedownloader.gogoanime.GogoanimeMainPage
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ScraperService(
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val subscribedAnimeService: SubscribedAnimeService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun retrieveData() {

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

        val allLinksFowDownload = gogoanimeEpisodePage.findAllLinksFowDownload(allLinksToAnimePages)

        logger.info("Anime download links found: $allLinksFowDownload.")
    }
}
