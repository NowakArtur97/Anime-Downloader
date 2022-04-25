package com.nowakartur.animedownloader

import com.nowakartur.animedownloader.gogoanime.GogoanimeMainPage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ScraperService(
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val subscribedAnimeService: SubscribedAnimeService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun retrieveData() {

        logger.info("Connecting to gogoanime page.")

        val page = gogoanimeMainPage.connectToPage()

        logger.info("Searching for all subscribed anime.")

        val allSubscribedAnimeLinks =
            gogoanimeMainPage.findAllSubscribedAnimeLinks(subscribedAnimeService.findAllSubscribedAnime(), page)

        logger.info("Anime found: $allSubscribedAnimeLinks")
    }
}
