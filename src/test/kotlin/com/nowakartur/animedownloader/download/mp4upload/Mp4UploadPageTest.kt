package com.nowakartur.animedownloader.download.mp4upload

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Mp4UploadPageTest {

    @Test
    fun `when check file size from Mp4Upload should return correct value`() {
        val mp4UploadUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        val expectedFileSize = 250.9f

        val actualFileSize = Mp4UploadPage.findFileSize(mp4UploadUrl)

        assertEquals(expectedFileSize, actualFileSize)
    }
}
