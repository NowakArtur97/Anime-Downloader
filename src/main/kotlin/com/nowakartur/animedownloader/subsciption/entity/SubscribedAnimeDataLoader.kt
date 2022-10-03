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
            SubscribedAnimeEntity("Shinmai Renkinjutsushi no Tenpo Keiei"),

//            TUESDAY
            SubscribedAnimeEntity("Shine Post", HIGH),

//            WEDNESDAY
            SubscribedAnimeEntity("Tensei Shitara Ken Deshita", MEDIUM),

//            THURSDAY

//            FRIDAY
//            SubscribedAnimeEntity("Hoshi no Samidare"),

//            SATURDAY
//            SubscribedAnimeEntity("Boku no Hero Academia 6", HIGH),
//            SubscribedAnimeEntity("Uzaki-chan wa Asobitai! Double"),
//            SubscribedAnimeEntity("Noumin Kanren no Skill Bakka Agetetara Nazeka Tsuyoku Natta"),
//            SubscribedAnimeEntity("Yuusha Party wo Tsuihou Sareta Beast Tamer, Saikyoushu no Nekomimi Shoujo to Deau"),
//            SubscribedAnimeEntity("Akuyaku Reijou nanode Last Boss wo Kattemimashita", HIGH),
//            SubscribedAnimeEntity("Spy x Family Part 2", HIGH),
//            SubscribedAnimeEntity("Koukyuu no Karasu"),
//            SubscribedAnimeEntity("Kakkou no Iinazuke"),
//            SubscribedAnimeEntity("Saikin Yatotta Maid ga Ayashii"),

//            SUNDAY
//            SubscribedAnimeEntity("Fuuto Tantei", HIGH),
        ).filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(anime)
    }
}
