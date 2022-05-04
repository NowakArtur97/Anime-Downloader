package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import org.junit.jupiter.api.Test

class DoodStreamPageSeleniumTest : SeleniumTest() {

    @Test
    fun `when download file from DoodStream should start downloading file`() {
        val episodeUrl = "https://dood.ws/d/2rho4ga0d14k"
        processTest(DoodStreamPage, episodeUrl)
    }
}
