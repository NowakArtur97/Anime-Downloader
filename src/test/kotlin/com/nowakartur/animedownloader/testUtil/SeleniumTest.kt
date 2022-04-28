package com.nowakartur.animedownloader.testUtil

import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

const val TIME_TO_WAIT_FOR_ASSERTION = 30L

@SpringBootTest
@TestPropertySource(properties = ["app.scheduler.enabled=false"])
class SeleniumTest {

    lateinit var webDriver: ChromeDriver

    @BeforeEach
    fun initializeWebDriver() {
        webDriver = SeleniumUtil.startWebDriver()
    }

    @AfterEach
    fun closeWebDriver() {
        webDriver.quit()
    }
}
