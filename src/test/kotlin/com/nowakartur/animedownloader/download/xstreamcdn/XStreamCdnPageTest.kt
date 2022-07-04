package com.nowakartur.animedownloader.download.xstreamcdn

import com.nowakartur.animedownloader.download.gogoanime.GOGOANIME_MAIN_PAGE_URL
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeEpisodePage
import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Test

class XStreamCdnPageTest : PageTest() {

    @Test
    fun `when check file size from XStreamCdn should return correct value`() {
        val xStreamCdnUrl = "https://fembed-hd.com/f/gl1enu-kl-px55r"
        val expectedFileSize = 249.49f

        processFileSizeTest(XStreamCdnPage, xStreamCdnUrl, expectedFileSize)
    }

    @Test
    fun `when prepare download link should return correct value`() {
        val expectedXStreamCdnUrl = "https://fembed-hd.com/f/wwnd5cny2mx4n3-"

        val page = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, "/yurei-deco-episode-1")

        processDownloadLinkTest(XStreamCdnPage, page, expectedXStreamCdnUrl)
    }
}
