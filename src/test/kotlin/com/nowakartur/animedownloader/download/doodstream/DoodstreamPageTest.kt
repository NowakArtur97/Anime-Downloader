package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.download.gogoanime.GOGOANIME_MAIN_PAGE_URL
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeEpisodePage
import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DoodstreamPageTest : PageTest() {

    @Test
    @Disabled
    fun `when check file size from Doodstream should return correct value`() {
        val doodstreamUrl = "https://dood.wf/e/qr35d3idb8wk"
        val expectedFileSize = 364.3f

        processFileSizeTest(DoodstreamPage, doodstreamUrl, expectedFileSize)
    }

    @Test
    @Disabled
    fun `when prepare download link should return correct value`() {
        val expectedDoodstreamUrl = "https://dood.wf/e/qr35d3idb8wk"

        val page = GogoanimeEpisodePage.connectToEpisodePage(
            GOGOANIME_MAIN_PAGE_URL,
            "/chiyu-mahou-no-machigatta-tsukaikata-senjou-wo-kakeru-kaifuku-youin-episode-5"
        )

        processDownloadLinkTest(DoodstreamPage, page, expectedDoodstreamUrl)
    }
}
