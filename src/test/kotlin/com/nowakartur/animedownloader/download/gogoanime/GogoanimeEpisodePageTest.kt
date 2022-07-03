package com.nowakartur.animedownloader.download.gogoanime

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val GOGOANIME_MAIN_PAGE_URL = "https://gogoanime.sk/"

class GogoanimeEpisodePageTest {

    @Test
    fun `when find links for download should return correct links`() {
        val linkToAnimePage = "love-all-play-episode-7"
        val episodePage = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, linkToAnimePage)
        val expectedLinks = listOf(
            "https://ssbstream.net/d/s2rrkvcv9nhl",
            "https://fembed-hd.com/f/ez40ni-62220den",
            "https://www.mp4upload.com/zdu7xouesiia.html",
        )
        val actualLinks = GogoanimeEpisodePage.findAllSupportedDownloadLinks(episodePage)

        assertEquals(expectedLinks, actualLinks)
    }
}
