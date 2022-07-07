package com.nowakartur.animedownloader.subsciption.entity

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority.HIGH
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority.MEDIUM
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SubscribedAnimeDataLoader(private val subscribedAnimeRepository: SubscribedAnimeRepository) {

    @EventListener(ApplicationReadyEvent::class)
    fun loadDataOnStartup() {
        val anime = listOf(
//            SubscribedAnimeEntity("Musashino"),
//            SubscribedAnimeEntity("Cap Kakumei Bottleman"),
//            SubscribedAnimeEntity("Super Dragon Ball Heroes"),
//            SubscribedAnimeEntity("Chiikawa"),
//            SubscribedAnimeEntity("Jantama Pong"),
//            SubscribedAnimeEntity("Kaginado Season 2"),
//            SubscribedAnimeEntity("Cyborg Kuro", MEDIUM),
//            SubscribedAnimeEntity("Chobits: Chibits", HIGH),
//            SubscribedAnimeEntity("Cap Kakumei Bottleman DX", HIGH),

//            MONDAY
//            SubscribedAnimeEntity("Youkoso Jitsuryoku Shijou Shugi no Kyoushitsu e", HIGH),
//            SubscribedAnimeEntity("Tensei Kenja no Isekai Life"),

//            TUESDAY
//            SubscribedAnimeEntity("Overlord IV", HIGH),
            SubscribedAnimeEntity("Kinsou no Vermeil"),
            SubscribedAnimeEntity("Tokyo Mew Mew New"),

//            WEDNESDAY
            SubscribedAnimeEntity("Mamahaha no Tsurego ga Motokano datta"),
            SubscribedAnimeEntity("Isekai Ojisan", MEDIUM),
            SubscribedAnimeEntity("Made in Abyss: Retsujitsu no Ougonkyou", HIGH),
            SubscribedAnimeEntity("Warau Arsnotoria Sun!"),
            SubscribedAnimeEntity("Isekai Meikyuu de Harem wo"),

//            THURSDAY
            SubscribedAnimeEntity("Kumicho Musume to Sewagakari"),
            SubscribedAnimeEntity("Summertime Render", HIGH),
            SubscribedAnimeEntity("Soredemo Ayumu wa Yosetekuru"),
            SubscribedAnimeEntity("Yofukashi no Uta", MEDIUM),

//            FRIDAY
            SubscribedAnimeEntity("Bucchigire!"),
            SubscribedAnimeEntity("Shadows House 2", MEDIUM),
            SubscribedAnimeEntity("Prima Doll"),
            SubscribedAnimeEntity("Hoshi no Samidare"),
            SubscribedAnimeEntity("Kami Kuzu", MEDIUM),
            SubscribedAnimeEntity("Kanojo, Okarishimasu"),

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
//            SubscribedAnimeEntity("Renmei Kuugun"),
//            SubscribedAnimeEntity("Yurei Deco"),
        ).filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(anime)
    }
}
