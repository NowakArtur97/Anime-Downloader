package com.nowakartur.animedownloader

import org.springframework.stereotype.Service

@Service
class SubscribedAnimeService {

    fun findAllSubscribedAnime(): List<String> =
        listOf(
            "Kono Healer, Mendokusai",
            "Otome Game Sekai wa Mob ni Kibishii Sekai desu",
            "Kakkou no Iinazuke",
        )
}
