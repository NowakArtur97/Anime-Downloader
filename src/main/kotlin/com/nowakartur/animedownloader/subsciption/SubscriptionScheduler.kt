package com.nowakartur.animedownloader.subsciption

import com.nowakartur.animedownloader.gogoanime.GogoanimeScraperService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled

class SubscriptionScheduler(
    private val gogoanimeScraperService: GogoanimeScraperService,
    private val subscribedAnimeService: SubscribedAnimeService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "\${app.scheduler.cron}")
    fun downloadAnime() {

        logger.info("Searching for all subscribed anime.")

        val subscribedAnime = subscribedAnimeService.findAllSubscribedAnime()

        gogoanimeScraperService.downloadAnime(subscribedAnime)
    }
}
