package com.nowakartur.animedownloader.testUtil

import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.awaitility.kotlin.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.TimeUnit

const val TIME_TO_WAIT_FOR_ASSERTION = 15L

@SpringBootTest
@TestPropertySource(properties = ["app.scheduler.enabled=false"])
class SeleniumTest {

    lateinit var webDriver: RemoteWebDriver

    @BeforeEach
    fun initializeWebDriver() {
        webDriver = SeleniumUtil.startWebDriver()
    }

    @AfterEach
    fun closeWebDriver() {
        webDriver.quit()
    }

    fun processTest(downloadPage: DownloadPage, url: String) {
        downloadPage.connectToDownloadPage(webDriver, url)

        downloadPage.downloadEpisode(webDriver)

        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}
