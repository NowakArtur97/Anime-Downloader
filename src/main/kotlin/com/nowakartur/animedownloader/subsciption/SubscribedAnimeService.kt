package com.nowakartur.animedownloader.subsciption

import org.springframework.stereotype.Service

@Service
class SubscribedAnimeService {

    fun findAllSubscribedAnime(): List<String> =
        listOf(
            "Araiguma Rascal",
//            "Kono Healer, Mendokusai",
//            "Otome Game Sekai wa Mob ni Kibishii Sekai desu",
//            "Kakkou no Iinazuke",
        )
}
