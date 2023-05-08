package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.download.gogoanime.GogoanimeScraperService
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@ConditionalOnProperty(
    value = ["app.scheduler.enabled"], havingValue = "true", matchIfMissing = true
)
class SubscriptionSchedulerConfig {

    @Bean
    fun subscriptionScheduler(
        gogoanimeScraperService: GogoanimeScraperService
    ): SubscribedAnimeDownloadScheduler = SubscribedAnimeDownloadScheduler(gogoanimeScraperService)

    @Bean
    fun statusResetScheduler(
        subscribedAnimeService: SubscribedAnimeService,
        @Value("\${app.scheduler.reset.number-of-days-after-to-reset}") numberOfDaysAfterToClean: Long
    ): SubscribedAnimeStatusResetScheduler =
        SubscribedAnimeStatusResetScheduler(subscribedAnimeService, numberOfDaysAfterToClean)

    @Bean
    fun failedStatusRemoveScheduler(
        subscribedAnimeService: SubscribedAnimeService,
    ): FailedAnimeCleanerScheduler = FailedAnimeCleanerScheduler(subscribedAnimeService)
}
