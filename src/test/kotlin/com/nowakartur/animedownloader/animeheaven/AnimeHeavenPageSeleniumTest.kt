package com.nowakartur.animedownloader.animeheaven

import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.awaitility.kotlin.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.TimeUnit

const val TIME_TO_WAIT_FOR_ASSERTION = 25L

@SpringBootTest
@TestPropertySource(properties = ["app.scheduler.enabled=false"])
class AnimeHeavenPageSeleniumTest {

    lateinit var webDriver: RemoteWebDriver

    @BeforeEach
    fun initializeWebDriver() {
        webDriver = SeleniumUtil.startWebDriver()
    }

    @AfterEach
    fun closeWebDriver() {
        webDriver.quit()
    }

    @Test
    fun `when download file from Anime Heaven should start downloading file`() {
        AnimeHeavenPage.connectToEpisodePage(webDriver, "https://animeheaven.me/anime.php?272gz")
        AnimeHeavenPage.selectNewestEpisode(webDriver)
        AnimeHeavenPage.downloadEpisode(webDriver)

        SeleniumUtil.switchToDownloadTab(webDriver)
        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            Assertions.assertTrue(SeleniumUtil.isDownloading(webDriver))
        }
    }
}
