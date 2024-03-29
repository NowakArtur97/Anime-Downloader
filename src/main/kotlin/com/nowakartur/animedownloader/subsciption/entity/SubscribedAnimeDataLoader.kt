package com.nowakartur.animedownloader.subsciption.entity

import com.nowakartur.animedownloader.csv.CsvDataLoader
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus.IN_PROGRESS
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus.TO_DOWNLOAD
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SubscribedAnimeDataLoader(
    private val subscribedAnimeRepository: SubscribedAnimeRepository,
    private val csvDataLoader: CsvDataLoader
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener(ApplicationReadyEvent::class)
    fun prepareDataOnStartup() {
        correctStatusOfAllTitlesInProgress()
        val animeListFromCsv = csvDataLoader.loadData()
        updateAfterChange(animeListFromCsv)
        saveNewTitles(animeListFromCsv)
        removeOldTitles(animeListFromCsv)
        logger.info("Data loading completed.")
    }

    private fun updateAfterChange(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val toUpdate = animeListFromCsv
            .mapNotNull { subscribedAnimeRepository.findByTitle(it.title) }
            .filter { entity ->
                val titleFromCsv = animeListFromCsv.find { it.title == entity.title }!!
                entity.hasChanged(titleFromCsv)
            }
            .map { entity ->
                val titleFromCsv = animeListFromCsv.find { it.title == entity.title }!!
                entity.update(titleFromCsv)
            }
        if (toUpdate.isNotEmpty()) {
            subscribedAnimeRepository.saveAll(toUpdate)
            logger.info("Successfully updated: ${toUpdate.map { it.title }}.")
        }
    }

    private fun saveNewTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val notInDb = animeListFromCsv.filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        if (notInDb.isNotEmpty()) {
            subscribedAnimeRepository.saveAll(notInDb)
            logger.info("Successfully added new titles: ${notInDb.map { it.title }}.")
        }
    }

    private fun removeOldTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val oldTitles = subscribedAnimeRepository.findAll()
            .filter { animeListFromCsv.none { animeFromCsv -> it.title == animeFromCsv.title } }
        if (oldTitles.isNotEmpty()) {
            subscribedAnimeRepository.deleteAll(oldTitles)
            logger.info("Successfully removed old titles: ${oldTitles.map { it.title }}.")
        }
    }

    private fun correctStatusOfAllTitlesInProgress() =
        subscribedAnimeRepository.updateAllStatusToStatus(IN_PROGRESS, TO_DOWNLOAD)
}
