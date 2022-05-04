package com.nowakartur.animedownloader.download.facade

import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.MP4_UPLOAD_TEXT
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.testUtil.SeleniumTest
import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class DownloadFacadeTest : SeleniumTest() {

    @Test
    fun `when download file in best quality should start downloading file from Mp4Upload`() {
        val goloadUrl = "https://goload.pro/download?id=MTg1NDY2&typesub=Gogoanime-SUB&title=Ao+Ashi+Episode+4"

        DownloadFacade.downloadInBestQuality(webDriver, goloadUrl)

        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(webDriver.currentUrl.contains(MP4_UPLOAD_TEXT))
        }
        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}