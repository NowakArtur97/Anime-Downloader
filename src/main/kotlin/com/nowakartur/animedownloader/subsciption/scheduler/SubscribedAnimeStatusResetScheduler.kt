package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

class SubscribedAnimeStatusResetScheduler(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val numberOfDaysAfterToClean: Long
) {

    @Scheduled(
        initialDelayString = "\${app.scheduler.reset.initial-delay-days}",
        fixedDelayString = "\${app.scheduler.reset.fixed-delay-days}",
        timeUnit = TimeUnit.DAYS
    )
    fun resetStatuses() {
        subscribedAnimeService.resetAnimeStatuses(numberOfDaysAfterToClean)
    }
}
