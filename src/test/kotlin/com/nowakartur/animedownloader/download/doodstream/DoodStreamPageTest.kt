package com.nowakartur.animedownloader.download.doodstream

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DoodStreamPageTest {

    @Test
    fun `when check file size from DoodStream should return correct value`() {
        val mp4UploadUrl = "https://dood.ws/d/2rho4ga0d14k"
        val expectedFileSize = 223.6f

        val actualFileSize = DoodStreamPage.findFileSize(mp4UploadUrl)

        assertEquals(expectedFileSize, actualFileSize)
    }
}
