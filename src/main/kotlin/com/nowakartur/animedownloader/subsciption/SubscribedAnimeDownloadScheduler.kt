package com.nowakartur.animedownloader.subsciption

import com.nowakartur.animedownloader.gogoanime.GogoanimeScraperService
import org.springframework.scheduling.annotation.Scheduled


class SubscribedAnimeDownloadScheduler(
    private val gogoanimeScraperService: GogoanimeScraperService
) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.initialDelay}",
        fixedDelayString = "\${app.scheduler.fixedDelay}"
    )
    fun downloadAnime() {

        gogoanimeScraperService.downloadAnime()
    }
}
