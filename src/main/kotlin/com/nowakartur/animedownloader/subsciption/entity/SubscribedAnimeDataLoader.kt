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
//            MONDAY
            SubscribedAnimeEntity("Youkoso Jitsuryoku Shijou Shugi no Kyoushitsu e", HIGH),
            SubscribedAnimeEntity("Tensei Kenja no Isekai Life", MEDIUM),
            SubscribedAnimeEntity("Orient: Awajishima Gekitou-hen"),

//            TUESDAY
            SubscribedAnimeEntity("Overlord IV", HIGH),
            SubscribedAnimeEntity("Kinsou no Vermeil", MEDIUM),
            SubscribedAnimeEntity("Tokyo Mew Mew New"),
            SubscribedAnimeEntity("Shine Post", HIGH),

//            WEDNESDAY
            SubscribedAnimeEntity("Warau Arsnotoria Sun!"),
            SubscribedAnimeEntity("Made in Abyss: Retsujitsu no Ougonkyou", HIGH),
            SubscribedAnimeEntity("Mamahaha no Tsurego ga Motokano datta"),
            SubscribedAnimeEntity("Isekai Meikyuu de Harem wo (Uncensored)", MEDIUM),
            SubscribedAnimeEntity("Isekai Ojisan", HIGH),

//            THURSDAY
            SubscribedAnimeEntity("Hataraku Maou-sama!!", MEDIUM),
            SubscribedAnimeEntity("Kumichou Musume to Sewagakari"),
            SubscribedAnimeEntity("Summertime Render", HIGH),
            SubscribedAnimeEntity("Yofukashi no Uta", HIGH),

//            FRIDAY
            SubscribedAnimeEntity("Bucchigire!"),
            SubscribedAnimeEntity("Soredemo Ayumu wa Yosetekuru", MEDIUM),
            SubscribedAnimeEntity("Prima Doll"),
            SubscribedAnimeEntity("Shadows House 2", HIGH),
            SubscribedAnimeEntity("Kami Kuzu", HIGH),
            SubscribedAnimeEntity("Hoshi no Samidare", MEDIUM),
            SubscribedAnimeEntity("Kanojo, Okarishimasu"),

//            SATURDAY
            SubscribedAnimeEntity("Love All Play"),
            SubscribedAnimeEntity("Ao Ashi", HIGH),
            SubscribedAnimeEntity("Teppen!", MEDIUM),
            SubscribedAnimeEntity("Kuro no Shoukansh"),
            SubscribedAnimeEntity("Shoot! Goal to the Future"),
            SubscribedAnimeEntity("Lycoris Recoil", HIGH),
            SubscribedAnimeEntity("Engage Kiss", MEDIUM),
            SubscribedAnimeEntity("Extreme Hearts", MEDIUM),
            SubscribedAnimeEntity("Kakkou no Iinazuke"),
            SubscribedAnimeEntity("Saikin Yatotta Maid ga Ayashii"),

//            SUNDAY
            SubscribedAnimeEntity("Renmei Kuugun", MEDIUM),
            SubscribedAnimeEntity("Isekai Yakkyoku", HIGH),
            SubscribedAnimeEntity("RWBY: Hyousetsu Teikoku", MEDIUM),
            SubscribedAnimeEntity("Yurei Deco"),
            SubscribedAnimeEntity("Fuuto Tantei", HIGH),
        ).filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(anime)
    }
}
