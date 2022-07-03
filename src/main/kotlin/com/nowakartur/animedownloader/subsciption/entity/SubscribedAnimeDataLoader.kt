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
//            SubscribedAnimeEntity("Musashino"),
//            SubscribedAnimeEntity("Super Dragon Ball Heroes"),
//            SubscribedAnimeEntity("Chiikawa"),
//            SubscribedAnimeEntity("Jantama Pong"),
//            SubscribedAnimeEntity("Kaginado Season 2"),
//            SubscribedAnimeEntity("Cyborg Kuro", MEDIUM),
//            SubscribedAnimeEntity("Chobits: Chibits", HIGH),
//            SubscribedAnimeEntity("Cap Kakumei Bottleman DX", HIGH),

//            MONDAY
//            SubscribedAnimeEntity("Kyoukai Senki Part 2"),
//            SubscribedAnimeEntity("Healer Girl", HIGH),

//            TUESDAY
//            SubscribedAnimeEntity("Yuusha, Yamemasu", MEDIUM),
//            SubscribedAnimeEntity("Tomodachi Game", HIGH),

//            WEDNESDAY
//            SubscribedAnimeEntity("Birdie Wing: Golf Girls' Story"),
//            SubscribedAnimeEntity("Shijou Saikyou no Daimaou, Murabito A ni Tensei suru"),
//            SubscribedAnimeEntity("RPG Fudousan"),
//            SubscribedAnimeEntity("Komi-san wa, Comyushou", HIGH),

//            THURSDAY
//            SubscribedAnimeEntity("Shachiku-san wa Youjo Yuurei ni Iyasaretai"),
//            SubscribedAnimeEntity("Paripi Koumei", HIGH),
//            SubscribedAnimeEntity("Gaikotsu Kishi"),
//            SubscribedAnimeEntity("Heroine Tarumono", MEDIUM),
//            SubscribedAnimeEntity("Summertime Render", HIGH),
//            SubscribedAnimeEntity("Mahoutsukai Reimeiki"),

//            FRIDAY
//            SubscribedAnimeEntity("Kanojo, Okarishimasu"),
//            SubscribedAnimeEntity("Kami Kuzu"),

//            SATURDAY
//            SubscribedAnimeEntity("Love All Play"),
//            SubscribedAnimeEntity("Ao Ashi", MEDIUM),
//            SubscribedAnimeEntity("Teppen!"),
//            SubscribedAnimeEntity("Shoot! Goal to the Future"),
//            SubscribedAnimeEntity("Engage Kiss"),
//            SubscribedAnimeEntity("Lycoris Recoil"),
//            SubscribedAnimeEntity("Kakkou no Iinazuke"),
//            SubscribedAnimeEntity("Kawaii dake ja Nai Shikimori-san"),

//            SUNDAY
//            SubscribedAnimeEntity("Kono Healer, Mendokusai", MEDIUM),
//            SubscribedAnimeEntity("Otome Game Sekai wa Mob ni Kibishii Sekai desu", HIGH),
//            SubscribedAnimeEntity("Baraou no Souretsu"),
//            SubscribedAnimeEntity("Black★★Rock Shooter: Dawn Fall"),
            SubscribedAnimeEntity("Renmei Kuugun"),
            SubscribedAnimeEntity("Yurei Deco"),
            SubscribedAnimeEntity("Prima Doll"),
        )
        subscribedAnimeRepository.saveAll(anime)
    }
}
