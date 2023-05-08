package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

class FailedAnimeCleanerScheduler(private val subscribedAnimeService: SubscribedAnimeService) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.failed.initial-delay-minutes}",
        fixedDelayString = "\${app.scheduler.failed.fixed-delay-minutes}",
        timeUnit = TimeUnit.MINUTES
    )
    fun removeFailedStatuses() {
        subscribedAnimeService.removeFailedAnimeStatuses()
    }
}
