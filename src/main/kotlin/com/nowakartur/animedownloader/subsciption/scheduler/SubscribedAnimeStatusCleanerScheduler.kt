package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

class SubscribedAnimeStatusCleanerScheduler(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val numberOfDaysAfterToClean: Long
) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.clean.initial-delay-days}",
        fixedDelayString = "\${app.scheduler.clean.fixed-delay-days}",
        timeUnit = TimeUnit.DAYS
    )
    fun changeStatus() {

        subscribedAnimeService.changeAnimeStatuses(numberOfDaysAfterToClean)
    }
}
