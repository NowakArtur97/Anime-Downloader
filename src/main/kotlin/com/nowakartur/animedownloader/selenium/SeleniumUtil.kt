package com.nowakartur.animedownloader.selenium

import com.nowakartur.animedownloader.selenium.JsScripts.CLICK_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.DOWNLOAD_PROGRESS_VALUE_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.DOWNLOAD_VIDEO_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.HAS_DOWNLOAD_STOPPED_DOWNLOAD_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.RESUME_DOWNLOAD_SCRIPT
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory

private const val WAIT_TIMEOUT_FOR_ELEMENT = 15L
private const val WAIT_FOR_DOWNLOAD_CHECK = 12_000L

object SeleniumUtil {

    private const val CHROME_DOWNLOADS = "chrome://downloads"
    private val HIDDEN_POSITION = Point(-1800, 0)

    private val logger = LoggerFactory.getLogger(javaClass)

    fun startWebDriver(): RemoteWebDriver {
        WebDriverManager.chromedriver().setup()
        val options = ChromeOptions().also {
            it.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking")) // disable all popups
            it.addArguments("lang=en-GB") // change language to English
        }
        return ChromeDriver(options).also {
            it.manage().window().position = HIDDEN_POSITION
        }
    }

    fun waitFor(webDriver: RemoteWebDriver, by: By) {
        val wait = WebDriverWait(webDriver, WAIT_TIMEOUT_FOR_ELEMENT)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by))
    }

    fun waitFor(webDriver: RemoteWebDriver, element: WebElement) {
        val wait = WebDriverWait(webDriver, WAIT_TIMEOUT_FOR_ELEMENT)
        wait.until(ExpectedConditions.visibilityOf(element))
    }

    fun clickUsingJavaScript(webDriver: RemoteWebDriver, element: WebElement) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript(CLICK_SCRIPT, element)
    }

    fun downloadVideoUsingJavaScript(webDriver: RemoteWebDriver) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript(DOWNLOAD_VIDEO_SCRIPT.trimMargin())
    }

    fun waitForFileDownload(driver: WebDriver, title: String) {
        switchToDownloadTab(driver)
        val jsExecutor = driver as JavascriptExecutor
        var percentage = 0L
        while (percentage < 100L) {
            try {
                if (hasDownloadStopped(jsExecutor)) {
                    logger.info("Download of [$title] stopped. Trying to resume.")
                    resumeDownload(jsExecutor)
                } else {
                    percentage = getDownloadProgress(jsExecutor)
                    logger.info("Download progress of: [$title] - $percentage%.")
                }
            } catch (e: Exception) {
                logger.info("Unexpected exception occurred when checking download progress of: [$title].")
                logger.info(e.message)
            }
            Thread.sleep(WAIT_FOR_DOWNLOAD_CHECK)
        }
    }

    fun isDownloading(webDriver: RemoteWebDriver): Boolean = getDownloadProgress(webDriver) > 0

    fun switchToDownloadTab(webDriver: WebDriver) {
        for (winHandle in webDriver.windowHandles) {
            webDriver.switchTo().window(winHandle)
        }
        webDriver[CHROME_DOWNLOADS]
    }

    private fun getDownloadProgress(jsExecutor: JavascriptExecutor): Long =
        jsExecutor.executeScript(DOWNLOAD_PROGRESS_VALUE_SCRIPT) as Long

    private fun hasDownloadStopped(jsExecutor: JavascriptExecutor): Boolean =
        jsExecutor.executeScript(HAS_DOWNLOAD_STOPPED_DOWNLOAD_SCRIPT) as Boolean

    private fun resumeDownload(jsExecutor: JavascriptExecutor) {
        jsExecutor.executeScript(RESUME_DOWNLOAD_SCRIPT)
    }
}
