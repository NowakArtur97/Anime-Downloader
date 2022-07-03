package com.nowakartur.animedownloader.subsciption.entity

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SubscribedAnimeService(
    private val subscribedAnimeRepository: SubscribedAnimeRepository,
) {

    fun findAllAnimeForDownload(): List<SubscribedAnimeEntity> =
        subscribedAnimeRepository.findByStatusIsOrderByPriorityDesc(SubscribedAnimeStatus.TO_DOWNLOAD)

    fun waitForDownload(subscribedAnimeEntity: SubscribedAnimeEntity) {
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

    fun changeAnimeStatuses(numberOfDaysAfterToClean: Long) {
        val updatedAnimeSubscriptions = subscribedAnimeRepository.findByStatusIsAndLastModifiedDateBefore(
            SubscribedAnimeStatus.DOWNLOADED,
            LocalDateTime.now().minusDays(numberOfDaysAfterToClean),
        )
        updatedAnimeSubscriptions.forEach {
            it.changeStatusToToDownload()
        }
        subscribedAnimeRepository.saveAll(updatedAnimeSubscriptions)
    }
}
