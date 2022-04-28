package com.nowakartur.animedownloader.m4upload

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
import com.nowakartur.animedownloader.util.SeleniumUtil
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.TimeUnit

class M4UploadPageTest(@Autowired private val m4UploadPage: M4UploadPage) : SeleniumTest() {

    @Test
    fun whenDownloadFileFromM4UploadShouldStartDownloadingFile() {
        val m4UploadUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        m4UploadPage.connectToDownloadPage(webDriver, m4UploadUrl)

        m4UploadPage.downloadEpisode(webDriver)

        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}
