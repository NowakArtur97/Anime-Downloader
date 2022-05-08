package com.nowakartur.animedownloader.download.mp4upload

import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Test

class Mp4UploadPageTest : PageTest() {

    @Test
    fun `when check file size from Mp4Upload should return correct value`() {
        val mp4UploadUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        val expectedFileSize = 250.9f

        processTest(Mp4UploadPage, mp4UploadUrl, expectedFileSize)
    }
}
