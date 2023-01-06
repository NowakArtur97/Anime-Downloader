package com.nowakartur.animedownloader.subsciption.entity

import com.nowakartur.animedownloader.csv.CsvDataLoader
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SubscribedAnimeDataLoader(
    private val subscribedAnimeRepository: SubscribedAnimeRepository,
    private val csvDataLoader: CsvDataLoader
) {

    @EventListener(ApplicationReadyEvent::class)
    fun loadDataOnStartup() {
        val anime = csvDataLoader.loadData()
            .filterNot { subscribedAnimeRepository.existsByTitle(it.title) }

        subscribedAnimeRepository.saveAll(anime)
    }
}
