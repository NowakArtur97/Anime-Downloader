package com.nowakartur.animedownloader.m4upload

import com.nowakartur.animedownloader.download.m4upload.M4UploadPage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class M4UploadPageTest {

    @Test
    fun `when check file size from M4Upload should return correct value`() {
        val m4UploadUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        val expectedFileSize = 250.9f

        val actualFileSize = M4UploadPage.findFileSize(m4UploadUrl)

        assertEquals(expectedFileSize, actualFileSize)
    }
}
