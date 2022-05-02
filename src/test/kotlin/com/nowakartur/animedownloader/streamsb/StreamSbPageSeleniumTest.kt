package com.nowakartur.animedownloader.streamsb

import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.testUtil.SeleniumTest
import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class StreamSbPageSeleniumTest : SeleniumTest() {

    @Test
    fun `when download file from StreamSb should start downloading file`() {
        val streamSbUrl = "https://sbplay2.xyz/d/j094za1pv1wg"
        StreamSbPage.connectToDownloadPage(webDriver, streamSbUrl)

        StreamSbPage.downloadEpisode(webDriver)

        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}
