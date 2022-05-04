package com.nowakartur.animedownloader.download.mp4upload

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import org.junit.jupiter.api.Test

class Mp4UploadPageSeleniumTest : SeleniumTest() {

    @Test
    fun `when download file from Mp4Upload should start downloading file`() {
        val episodeUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        processTest(Mp4UploadPage, episodeUrl)
    }
}
