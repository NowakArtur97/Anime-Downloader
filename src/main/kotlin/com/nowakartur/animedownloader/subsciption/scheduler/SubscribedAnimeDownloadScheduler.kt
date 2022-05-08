package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.download.gogoanime.GogoanimeScraperService
import org.springframework.scheduling.annotation.Scheduled


class SubscribedAnimeDownloadScheduler(
    private val gogoanimeScraperService: GogoanimeScraperService
) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.download.initialDelayMs}",
        fixedDelayString = "\${app.scheduler.download.fixedDelayMs}",
    )
    fun downloadAnime() {

        gogoanimeScraperService.downloadAnime()
    }
}
