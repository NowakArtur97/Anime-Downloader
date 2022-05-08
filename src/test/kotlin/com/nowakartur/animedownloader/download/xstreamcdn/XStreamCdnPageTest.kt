package com.nowakartur.animedownloader.download.xstreamcdn

import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Test

class XStreamCdnPageTest : PageTest() {

    @Test
    fun `when check file size from XStreamCdn should return correct value`() {
        val xStreamCdnUrl = "https://fembed-hd.com/f/gl1enu-kl-px55r"
        val expectedFileSize = 249.49f

        processTest(XStreamCdnPage, xStreamCdnUrl, expectedFileSize)
    }
}
