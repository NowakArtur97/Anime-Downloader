package com.nowakartur.animedownloader.download.gogoanime

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val GOGOANIME_MAIN_PAGE_URL = "https://gogoanime.sk/"

class GogoanimeEpisodePageTest {

    @Test
    fun `when find link for download should return correct link`() {
        val linkToAnimePage = "/love-all-play-episode-7"
        val episodePage = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, linkToAnimePage)
        val goloadLinkExpected =
            "https://goload.pro/download?id=MTg2NDIw&typesub=Gogoanime-SUB&title=Love+All+Play+Episode+7"

        val goloadLinkActual = GogoanimeEpisodePage.findLinkForDownload(episodePage)

        assertEquals(goloadLinkExpected, goloadLinkActual)
    }
}
