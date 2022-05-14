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
        if (subscribedAnimeRepository.count() > 0) {
            return
        }

        val anime = listOf(
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
            SubscribedAnimeEntity("Koi wa Sekai Seifuku no Ato de"),
            SubscribedAnimeEntity("Kaguya-sama wa Kokurasetai", HIGH),
            SubscribedAnimeEntity("Shokei Shoujo no Virgin Road"),
            SubscribedAnimeEntity("Aharen-san wa Hakarenai", MEDIUM),
            SubscribedAnimeEntity("Dance Dance Danseur", MEDIUM),

//            SATURDAY
//            SubscribedAnimeEntity("Love All Play"),
//            SubscribedAnimeEntity("Ao Ashi", MEDIUM),
//            SubscribedAnimeEntity("Spy x Family", HIGH),
//            SubscribedAnimeEntity("Gunjou no Fanfare"),
//            SubscribedAnimeEntity("Kunoichi Tsubaki no Mune no Uchi"),
//            SubscribedAnimeEntity("Build Divide: Code White"),
//            SubscribedAnimeEntity("Kakkou no Iinazuke"),
//            SubscribedAnimeEntity("Kawaii dake ja Nai Shikimori-san"),

//            SUNDAY
//            SubscribedAnimeEntity("Kono Healer, Mendokusai"),
//            SubscribedAnimeEntity("Otome Game Sekai wa Mob ni Kibishii Sekai desu", HIGH),
//            SubscribedAnimeEntity("Baraou no Souretsu"),
//            SubscribedAnimeEntity("Black★★Rock Shooter: Dawn Fall"),
        )
        subscribedAnimeRepository.saveAll(anime)
    }
}
