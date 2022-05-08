package com.nowakartur.animedownloader.download.xstreamcdn

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import org.junit.jupiter.api.Test

class XStreamCdnPageSeleniumTest : SeleniumTest() {

    @Test
    fun `when download file from XStreamCdn should start downloading file`() {
        val episodeUrl = "https://fembed-hd.com/f/gl1enu-kl-px55r"
        processTest(XStreamCdnPage, episodeUrl)
    }
}
