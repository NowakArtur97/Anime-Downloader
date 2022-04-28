package com.nowakartur.animedownloader.subsciption

import org.springframework.stereotype.Service

@Service
class SubscribedAnimeService(private val subscribedAnimeRepository: SubscribedAnimeRepository) {

    fun findAllAnimeForDownload(): List<SubscribedAnimeEntity> =
        subscribedAnimeRepository.findByStatusIsOrderByPriorityDesc(SubscribedAnimeStatus.TO_DOWNLOAD)

    fun startDownloadingAnime(subscribedAnimeEntity: SubscribedAnimeEntity) {
        subscribedAnimeEntity.startDownloading()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }

    fun finishDownloadingAnime(subscribedAnimeEntity: SubscribedAnimeEntity) {
        subscribedAnimeEntity.finishDownloading()
        subscribedAnimeRepository.save(subscribedAnimeEntity)
    }
}
