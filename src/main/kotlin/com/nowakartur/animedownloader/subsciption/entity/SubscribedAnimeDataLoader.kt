package com.nowakartur.animedownloader.subsciption.entity

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority.HIGH
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority.LOW
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
//            SubscribedAnimeEntity("Kaginado Season 2", LOW),
//            SubscribedAnimeEntity("Cyborg Kuro"),
//            SubscribedAnimeEntity("Chobits: Chibits", HIGH),
            SubscribedAnimeEntity("Cap Kakumei Bottleman DX", HIGH),

//            MONDAY
            SubscribedAnimeEntity("Kyoukai Senki Part 2", LOW),
            SubscribedAnimeEntity("Healer Girl", HIGH),

//            SubscribedAnimeEntity("Komi-san wa, Comyushou", HIGH),

//            THURSDAY
//            SubscribedAnimeEntity("Shachiku-san wa Youjo Yuurei ni Iyasaretai", LOW),
//            SubscribedAnimeEntity("Paripi Koumei", HIGH),
//            SubscribedAnimeEntity("Gaikotsu Kishi"),
//            SubscribedAnimeEntity("Heroine Tarumono"),
//            SubscribedAnimeEntity("Summertime Render", HIGH),
//            SubscribedAnimeEntity("Mahoutsukai Reimeiki"),

//            FRIDAY
//            SubscribedAnimeEntity("Koi wa Sekai Seifuku no Ato de", LOW),
//            SubscribedAnimeEntity("Kaguya-sama wa Kokurasetai", HIGH),
//            SubscribedAnimeEntity("Shokei Shoujo no Virgin Road", LOW),
//            SubscribedAnimeEntity("Aharen-san wa Hakarenai"),
//            SubscribedAnimeEntity("Dance Dance Danseur"),

//            SATURDAY
//            SubscribedAnimeEntity("Love All Play", LOW),
//            SubscribedAnimeEntity("Ao Ashi"),
//            SubscribedAnimeEntity("Spy x Family", HIGH),
//            SubscribedAnimeEntity("Gunjou no Fanfare", LOW),
//            SubscribedAnimeEntity("Kunoichi Tsubaki no Mune no Uchi", LOW),
//            SubscribedAnimeEntity("Build Divide: Code White", LOW),
//            SubscribedAnimeEntity("Kakkou no Iinazuke", LOW),
//            SubscribedAnimeEntity("Kawaii dake ja Nai Shikimori-san", LOW),

//            SUNDAY
//            SubscribedAnimeEntity("Kono Healer, Mendokusai", LOW),
//            SubscribedAnimeEntity("Otome Game Sekai wa Mob ni Kibishii Sekai desu", HIGH),
//            SubscribedAnimeEntity("Baraou no Souretsu", LOW),
//            SubscribedAnimeEntity("Black★★Rock Shooter: Dawn Fall", LOW),
        )
        subscribedAnimeRepository.saveAll(anime)
    }
}
