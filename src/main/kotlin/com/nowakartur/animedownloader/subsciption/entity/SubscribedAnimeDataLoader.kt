package com.nowakartur.animedownloader.subsciption.entity

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
//            SubscribedAnimeEntity("Cyborg Kuro"),
//            SubscribedAnimeEntity("Chobits: Chibits", HIGH),
//            SubscribedAnimeEntity("Shachiku-san wa Youjo Yuurei ni Iyasaretai", LOW),
//            SubscribedAnimeEntity("Gaikotsu Kishi"),
//            SubscribedAnimeEntity("Heroine Tarumono"),
//            SubscribedAnimeEntity("Mahoutsukai Reimeiki"),
            SubscribedAnimeEntity("Summertime Render", SubscribedAnimePriority.HIGH),
//            SubscribedAnimeEntity("Komi-san wa, Comyushou"),
//            SubscribedAnimeEntity("Dance Dance Danseur"),
//            SubscribedAnimeEntity("Aharen-san wa Hakarenai"),
//            SubscribedAnimeEntity("Kaguya-sama wa Kokurasetai", HIGH),
//            SubscribedAnimeEntity("Koi wa Sekai Seifuku no Ato de", LOW),
//            SubscribedAnimeEntity("Shokei Shoujo no Virgin Road", LOW),
//            SubscribedAnimeEntity("Otome Game Sekai wa Mob ni Kibishii Sekai desu", HIGH),
//            SubscribedAnimeEntity("Paripi Koumei", HIGH),
//            SubscribedAnimeEntity("Kaginado Season 2", LOW),
        )
        subscribedAnimeRepository.saveAll(anime)
    }
}
