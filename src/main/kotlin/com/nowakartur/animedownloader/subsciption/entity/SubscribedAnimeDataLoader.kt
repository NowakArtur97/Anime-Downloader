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
            SubscribedAnimeEntity("Cool Doji Danshi"),
            SubscribedAnimeEntity("Eternal Boys"),

//            TUESDAY
            SubscribedAnimeEntity("Shinobi no Ittoki"),
            SubscribedAnimeEntity("Chainsaw Man", HIGH),

//            WEDNESDAY
            SubscribedAnimeEntity("Kage no Jitsuryokusha ni Naritakute!", HIGH),
            SubscribedAnimeEntity("Futoku no Guild (Uncensored)"),
            SubscribedAnimeEntity("Tensei Shitara Ken Deshita", MEDIUM),
            SubscribedAnimeEntity("Do It Yourself!!"),
            SubscribedAnimeEntity("Mob Psycho 100 III", HIGH),
            SubscribedAnimeEntity("Renai Flops", MEDIUM),

//            THURSDAY
            SubscribedAnimeEntity("Isekai Ojisan", HIGH),
            SubscribedAnimeEntity("Mushikaburi-hime"),
            SubscribedAnimeEntity("Urusei Yatsura", MEDIUM),
            SubscribedAnimeEntity("Akiba Maid Sensou", MEDIUM),

//            FRIDAY
            SubscribedAnimeEntity("Megaton-kyuu Musashi 2"),
            SubscribedAnimeEntity("Uchi no Shishou wa Shippo ga Nai"),
            SubscribedAnimeEntity("Hoshi no Samidare"),

//            SATURDAY
            SubscribedAnimeEntity("Mairimashita! Iruma-kun 3", HIGH),
            SubscribedAnimeEntity("Boku no Hero Academia 6", HIGH),
            SubscribedAnimeEntity("Noumin Kanren no Skill Bakka Agetetara Nazeka Tsuyoku Natta"),
            SubscribedAnimeEntity("Yuusha Party wo Tsuihou Sareta Beast Tamer, Saikyoushu no Nekomimi Shoujo to Deau"),
            SubscribedAnimeEntity("Akuyaku Reijou nanode Last Boss wo Kattemimashita", HIGH),
            SubscribedAnimeEntity("Uzaki-chan wa Asobitai! Double"),
            SubscribedAnimeEntity("Bocchi the Rock!", MEDIUM),
            SubscribedAnimeEntity("Spy x Family Part 2", HIGH),
            SubscribedAnimeEntity("Koukyuu no Karasu"),
            SubscribedAnimeEntity("4-nin wa Sorezore Uso wo Tsuku"),
            SubscribedAnimeEntity("Blue Lock", HIGH),
            SubscribedAnimeEntity("Yowamushi Pedal: Limit Break", MEDIUM),

//            SUNDAY
            SubscribedAnimeEntity("Mobile Suit Gundam: The Witch from Mercury"),
        ).filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(anime)
    }
}
