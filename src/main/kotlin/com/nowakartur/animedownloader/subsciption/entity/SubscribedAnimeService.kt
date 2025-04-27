package com.nowakartur.animedownloader.subsciption.entity

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SubscribedAnimeService(
    private val subscribedAnimeRepository: SubscribedAnimeRepository,
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun findAllAnimeForDownload(): List<SubscribedAnimeEntity> =
        subscribedAnimeRepository.findByStatusIsOrderByPriorityDesc(SubscribedAnimeStatus.TO_DOWNLOAD)

    fun waitForDownloadingAnime(subscribedAnimeEntity: SubscribedAnimeEntity) {
        subscribedAnimeEntity.changeStatusToToDownload()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }

    fun startDownloadingAnime(subscribedAnimeEntity: SubscribedAnimeEntity) {
        subscribedAnimeEntity.changeStatusToInProgress()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }

    fun finishDownloadingAnime(subscribedAnimeEntity: SubscribedAnimeEntity) {
        subscribedAnimeEntity.changeStatusToDownloaded()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }

    fun finishDownloadingAnime(subscribedAnimeEntity: SubscribedAnimeEntity, episodeNumber: Int) {
        subscribedAnimeEntity.episodeNumber = episodeNumber
        subscribedAnimeEntity.changeStatusToDownloaded()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }

    fun setAsFailedAnimeDownload(subscribedAnimeEntity: SubscribedAnimeEntity) {
        subscribedAnimeEntity.changeStatusToFailed()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }

    fun resetAnimeStatuses(numberOfDaysAfterToClean: Long) {
        val updatedAnimeSubscriptions = subscribedAnimeRepository.findByStatusIsAndLastModifiedDateBefore(
            SubscribedAnimeStatus.DOWNLOADED,
            LocalDateTime.now().minusDays(numberOfDaysAfterToClean),
        )
        if (updatedAnimeSubscriptions.isNotEmpty()) {
            changeToDownloadStatusesAndSave(updatedAnimeSubscriptions)
            logger.info("Titles with reseted status to TO_DOWNLOAD: ${updatedAnimeSubscriptions.map { it.title }}.")
        }
    }

    fun removeFailedAnimeStatuses() {
        val updatedAnimeSubscriptions = subscribedAnimeRepository.findByStatusIs(SubscribedAnimeStatus.FAILED)
        if (updatedAnimeSubscriptions.isNotEmpty()) {
            changeToDownloadStatusesAndSave(updatedAnimeSubscriptions)
            logger.info("Titles with removed FAILED status: ${updatedAnimeSubscriptions.map { it.title }}.")
        }
    }

    private fun changeToDownloadStatusesAndSave(updatedAnimeSubscriptions: List<SubscribedAnimeEntity>) {
        updatedAnimeSubscriptions.forEach { it.changeStatusToToDownload() }
        subscribedAnimeRepository.saveAll(updatedAnimeSubscriptions)
    }
}
