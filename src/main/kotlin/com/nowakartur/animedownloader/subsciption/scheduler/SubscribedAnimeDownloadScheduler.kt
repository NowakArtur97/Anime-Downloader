package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.animeheaven.AnimeHeavenDownloadService
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeScraperService
import org.springframework.scheduling.annotation.Scheduled


class SubscribedAnimeDownloadScheduler(
    private val gogoanimeScraperService: GogoanimeScraperService,
    private val animeHeavenDownloadService: AnimeHeavenDownloadService
) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.download.initial-delay-ms}",
        fixedDelayString = "\${app.scheduler.download.fixed-delay-ms}",
    )
    fun downloadAnime() {
//        gogoanimeScraperService.downloadAnime()
        animeHeavenDownloadService.downloadAnime()
    }
}
