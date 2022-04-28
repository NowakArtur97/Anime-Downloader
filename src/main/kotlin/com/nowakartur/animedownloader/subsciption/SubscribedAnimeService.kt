package com.nowakartur.animedownloader.subsciption

import org.springframework.stereotype.Service

@Service
class SubscribedAnimeService(private val subscribedAnimeRepository: SubscribedAnimeRepository) {

    fun findAllSubscribedAnime(): List<SubscribedAnimeEntity> = subscribedAnimeRepository.findAll()
}
