package com.nowakartur.animedownloader.download.mp4upload

import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.testUtil.SeleniumTest
import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class Mp4UploadPageSeleniumTest : SeleniumTest() {


    @Test
    fun `when download file from Mp4Upload should start downloading file`() {
        val mp4UploadUrl = "https://www.mp4upload.com/6gy7hh1itnhe"
        Mp4UploadPage.connectToDownloadPage(webDriver, mp4UploadUrl)

        Mp4UploadPage.downloadEpisode(webDriver)

        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}
