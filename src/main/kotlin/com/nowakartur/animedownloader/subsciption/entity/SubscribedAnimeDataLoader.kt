package com.nowakartur.animedownloader.subsciption.entity

import com.nowakartur.animedownloader.csv.CsvDataLoader
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus.IN_PROGRESS
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus.TO_DOWNLOAD
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
        correctStatusOfAllTitlesInProgress()
        val animeListFromCsv = csvDataLoader.loadData()
        updatePriorities(animeListFromCsv)
        saveNewTitles(animeListFromCsv)
        removeOldTitles(animeListFromCsv)
    }

    private fun updatePriorities(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val toUpdatePriorities = animeListFromCsv
            .mapNotNull { subscribedAnimeRepository.findByTitle(it.title) }
            .filter { entity ->
                val titleFromCsv = animeListFromCsv.find { it.title == entity.title }
                entity.priority != titleFromCsv!!.priority
            }
            .map { entity ->
                val titleFromCsv = animeListFromCsv.find { it.title == entity.title }
                entity.also { entity.priority = titleFromCsv!!.priority }
            }
        subscribedAnimeRepository.saveAll(toUpdatePriorities)
    }

    private fun saveNewTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val notInDb = animeListFromCsv.filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(notInDb)
    }

    private fun removeOldTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val oldTitles = subscribedAnimeRepository.findAll()
            .filter { animeListFromCsv.none { animeFromCsv -> it.title == animeFromCsv.title } }
        subscribedAnimeRepository.deleteAll(oldTitles)
    }

    private fun correctStatusOfAllTitlesInProgress() =
        subscribedAnimeRepository.updateAllStatusToStatus(IN_PROGRESS, TO_DOWNLOAD)
}
