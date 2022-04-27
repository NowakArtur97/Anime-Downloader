package com.nowakartur.animedownloader.subsciption

import com.nowakartur.animedownloader.gogoanime.GogoanimeScraperService
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
        gogoanimeScraperService: GogoanimeScraperService,
        subscribedAnimeService: SubscribedAnimeService,
    ): SubscriptionScheduler = SubscriptionScheduler(gogoanimeScraperService, subscribedAnimeService)
}
