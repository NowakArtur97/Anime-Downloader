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
            SubscribedAnimeEntity("Cool Doji Danshi"),
            SubscribedAnimeEntity("Eternal Boys"),

//            TUESDAY
            SubscribedAnimeEntity("Chainsaw Man", HIGH),

//            WEDNESDAY
//            SubscribedAnimeEntity("Tensei Oujo to Tensai Reijou no Mahou Kakumei", MEDIUM),
//            SubscribedAnimeEntity("Kage no Jitsuryokusha ni Naritakute!", HIGH),
//            SubscribedAnimeEntity("Renai Flops"),

//            THURSDAY
            SubscribedAnimeEntity("Isekai Ojisan", HIGH),
            SubscribedAnimeEntity("Urusei Yatsura", MEDIUM),

//            FRIDAY
            SubscribedAnimeEntity("Megaton-kyuu Musashi 2"),

//            SATURDAY
//            SubscribedAnimeEntity("Boku no Hero Academia 6", MEDIUM),
//			SubscribedAnimeEntity("Mairimashita! Iruma-kun 3", MEDIUM),
//            SubscribedAnimeEntity("Blue Lock", HIGH),
            SubscribedAnimeEntity("Yowamushi Pedal: Limit Break", HIGH),

//            SUNDAY
//            SubscribedAnimeEntity("Mobile Suit Gundam: The Witch from Mercury"),
            SubscribedAnimeEntity("Fumetsu no Anata e 2"),
        ).filterNot { subscribedAnimeRepository.existsByTitle(it.title) }
        subscribedAnimeRepository.saveAll(anime)
    }
}
