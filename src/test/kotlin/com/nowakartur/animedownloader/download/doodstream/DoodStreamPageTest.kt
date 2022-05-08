package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Test

class DoodStreamPageTest : PageTest() {

    @Test
    fun `when check file size from DoodStream should return correct value`() {
        val doodStreamUrl = "https://dood.ws/d/2rho4ga0d14k"
        val expectedFileSize = 223.6f

        processTest(DoodStreamPage, doodStreamUrl, expectedFileSize)
    }
}
