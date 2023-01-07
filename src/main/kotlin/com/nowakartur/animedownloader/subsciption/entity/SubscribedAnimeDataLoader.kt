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
        updatePriorities(animeListFromCsv)
        saveNewTitles(animeListFromCsv)
        removeOldTitles(animeListFromCsv)
        logger.info("Data loading completed.")
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
        if (toUpdatePriorities.isNotEmpty()) {
            subscribedAnimeRepository.saveAll(toUpdatePriorities)
            logger.info("Successfully updated title priorities: ${toUpdatePriorities.map { it.title }}.")
        } else {
            logger.info("There are no titles to update priorities.")
        }
    }

    private fun saveNewTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val notInDb = animeListFromCsv.filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        if (notInDb.isNotEmpty()) {
            subscribedAnimeRepository.saveAll(notInDb)
            logger.info("Successfully added new titles: ${notInDb.map { it.title }}.")
        } else {
            logger.info("There are no new titles to save.")
        }
    }

    private fun removeOldTitles(animeListFromCsv: List<SubscribedAnimeEntity>) {
        val oldTitles = subscribedAnimeRepository.findAll()
            .filter { animeListFromCsv.none { animeFromCsv -> it.title == animeFromCsv.title } }
        if (oldTitles.isNotEmpty()) {
            subscribedAnimeRepository.deleteAll(oldTitles)
            logger.info("Successfully removed old titles: ${oldTitles.map { it.title }}.")
        } else {
            logger.info("There are no old titles to remove.")
        }
    }

    private fun correctStatusOfAllTitlesInProgress() =
        subscribedAnimeRepository.updateAllStatusToStatus(IN_PROGRESS, TO_DOWNLOAD)
}
