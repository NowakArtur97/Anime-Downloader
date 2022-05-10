package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.download.gogoanime.GogoanimeScraperService
import org.springframework.scheduling.annotation.Scheduled


class SubscribedAnimeDownloadScheduler(
    private val gogoanimeScraperService: GogoanimeScraperService
) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.download.initial-delay-ms}",
        fixedDelayString = "\${app.scheduler.download.fixed-delay-ms}",
    )
    fun downloadAnime() {

        gogoanimeScraperService.downloadAnime()
    }
}
