package com.nowakartur.animedownloader.m4upload

import com.nowakartur.animedownloader.download.m4upload.M4UploadPage
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.testUtil.SeleniumTest
import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class M4UploadPageSeleniumTest : SeleniumTest() {


    @Test
    fun `when download file from M4Upload should start downloading file`() {
        val m4UploadUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        M4UploadPage.connectToDownloadPage(webDriver, m4UploadUrl)

        M4UploadPage.downloadEpisode(webDriver)

        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}
