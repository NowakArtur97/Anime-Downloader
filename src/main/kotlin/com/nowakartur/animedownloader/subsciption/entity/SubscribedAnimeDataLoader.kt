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
        val animeListFromCsv = saveNewTitles()
        removeOldTitles(animeListFromCsv)
        correctStatusOfAllTitlesInProgress()
    }

    private fun saveNewTitles(): List<SubscribedAnimeEntity> {
        val animeListFromCsv = csvDataLoader.loadData()
        val notInDb = animeListFromCsv.filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(notInDb)
        return animeListFromCsv
    }

    private fun removeOldTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        subscribedAnimeRepository.findAll()
            .filter { animeListFromCsv.none { animeFromCsv -> it.title == animeFromCsv.title } }
            .forEach { subscribedAnimeRepository.delete(it) }
    }

    private fun correctStatusOfAllTitlesInProgress() {
        val allInProgress = subscribedAnimeRepository.findAllByStatus(
            SubscribedAnimeStatus.IN_PROGRESS
        ).map {
            it.changeStatusToToDownload()
            it
        }
        subscribedAnimeRepository.saveAll(allInProgress)
    }
}
