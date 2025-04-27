package com.nowakartur.animedownloader.animeheaven

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AnimeHeavenPageTest {

    @Test
    fun `when get all anime download info should return info about all the latest episodes`() {

        val allAnimeDownloadInfo = AnimeHeavenPage.getAllAnimeDownloadInfo("https://animeheaven.me/new.php")

        assertEquals(71, allAnimeDownloadInfo.size)
    }
}
