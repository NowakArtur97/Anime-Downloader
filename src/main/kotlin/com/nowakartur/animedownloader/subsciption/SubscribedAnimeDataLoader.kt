package com.nowakartur.animedownloader.subsciption

import com.nowakartur.animedownloader.subsciption.SubscribedAnimePriority.HIGH
import com.nowakartur.animedownloader.subsciption.SubscribedAnimePriority.LOW
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SubscribedAnimeDataLoader(private val subscribedAnimeRepository: SubscribedAnimeRepository) {

    @EventListener(ApplicationReadyEvent::class)
    fun loadDataOnStartup() {
        if (subscribedAnimeRepository.count() > 0) {
            return
        }

        val anime = listOf(
            SubscribedAnimeEntity("RPG Fudousan"),
            SubscribedAnimeEntity("Araiguma Rascal", LOW),
            SubscribedAnimeEntity("Shijou Saikyou no Daimaou, Murabito A ni Tensei", HIGH),
            SubscribedAnimeEntity("Summertime Render", HIGH),
            SubscribedAnimeEntity("Otome Game Sekai wa Mob ni Kibishii Sekai desu", HIGH),
//            SubscribedAnimeEntity("Paripi Koumei", HIGH),
//            SubscribedAnimeEntity("Kaginado Season 2", LOW),
        )
        subscribedAnimeRepository.saveAll(anime)
    }
}
