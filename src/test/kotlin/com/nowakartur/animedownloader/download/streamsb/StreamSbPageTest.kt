package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.download.gogoanime.GOGOANIME_MAIN_PAGE_URL
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeEpisodePage
import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Test

class StreamSbPageTest : PageTest() {

    @Test
    fun `when check file size with three quality options from StreamSb should return correct value`() {
        val streamSbUrl = "https://sbplay2.xyz/d/j094za1pv1wg"
        val expectedFileSize = 250.9f

        processFileSizeTest(StreamSbPage, streamSbUrl, expectedFileSize)
    }

    @Test
    fun `when check file size with two quality options from StreamSb should return correct value`() {
        val streamSbUrl = "https://sbplay2.xyz/d/n9oomq4pecsi"
        val expectedFileSize = 113.5f

        processFileSizeTest(StreamSbPage, streamSbUrl, expectedFileSize)
    }

    @Test
    fun `when prepare download link should return correct value`() {
        val expectedStreamSbUrl = "https://ssbstream.net/d/9e6sw6cx06sb"

        val page = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, "/yurei-deco-episode-1")

        processDownloadLinkTest(StreamSbPage, page, expectedStreamSbUrl)
    }
}
