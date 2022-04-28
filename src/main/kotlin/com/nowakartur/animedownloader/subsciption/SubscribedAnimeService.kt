package com.nowakartur.animedownloader.subsciption

import org.springframework.stereotype.Service

@Service
class SubscribedAnimeService {

    fun findAllSubscribedAnime(): List<String> =
        listOf(
            "Shijou Saikyou no Daimaou, Murabito A ni Tensei",
//            "RPG Fudousan",
//            "Paripi Koumei",
//            "Summertime Render",
//            "Kaginado Season 2",
//            "Araiguma Rascal",
//            "Kono Healer, Mendokusai",
//            "Otome Game Sekai wa Mob ni Kibishii Sekai desu",
//            "Kakkou no Iinazuke",
        )
}
