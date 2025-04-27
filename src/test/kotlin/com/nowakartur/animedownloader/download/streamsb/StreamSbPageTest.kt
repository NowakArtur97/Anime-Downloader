package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.download.gogoanime.GOGOANIME_MAIN_PAGE_URL
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeEpisodePage
import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class StreamSbPageTest : PageTest() {

    @Test
    @Disabled
    fun `when check file size from StreamSb should return correct value`() {
        val streamSbUrl = "https://awish.pro/f/bfwsast5z8lw_o"
        val expectedFileSize = 186.8f

        processFileSizeTest(StreamSbPage, streamSbUrl, expectedFileSize)
    }


    @Test
    @Disabled
    fun `when prepare download link should return correct value`() {
        val expectedStreamSbUrl = "https://awish.pro/f/dfda5bz7grd8_x"

        val page = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, "/yurei-deco-episode-1")

        processDownloadLinkTest(StreamSbPage, page, expectedStreamSbUrl)
    }
}
