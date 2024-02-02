package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import org.junit.jupiter.api.Test

class DoodstreamPageSeleniumTest : SeleniumTest() {

    @Test
    fun `when download file from Doodstream should start downloading file`() {
        val episodeUrl = "https://dood.wf/e/qr35d3idb8wk"
        processTest(DoodstreamPage, episodeUrl)
    }
}
