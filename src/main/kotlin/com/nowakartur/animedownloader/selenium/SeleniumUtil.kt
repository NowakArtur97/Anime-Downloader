package com.nowakartur.animedownloader.selenium

import com.nowakartur.animedownloader.selenium.JsScripts.CLICK_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.DOWNLOAD_PROGRESS_VALUE_SCRIPT
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory


private const val WAIT_TIMEOUT_FOR_ELEMENT = 15L
private const val WAIT_FOR_DOWNLOAD_CHECK = 5_000L

object SeleniumUtil {

    private const val CHROME_DOWNLOADS = "chrome://downloads"
    private val HIDDEN_POSITION = Point(-1800, 0)

    private val logger = LoggerFactory.getLogger(javaClass)

    fun startWebDriver(): ChromeDriver {
        WebDriverManager.chromedriver().setup()
        val options = ChromeOptions().also {
            it.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking")) // disable all popups
        }
        return ChromeDriver(options).also {
//            it.manage().window().position = HIDDEN_POSITION
        }
    }

    fun waitFor(webDriver: ChromeDriver, by: By) {
        val wait = WebDriverWait(webDriver, WAIT_TIMEOUT_FOR_ELEMENT)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by))
    }

    fun waitFor(webDriver: ChromeDriver, element: WebElement) {
        val wait = WebDriverWait(webDriver, WAIT_TIMEOUT_FOR_ELEMENT)
        wait.until(ExpectedConditions.visibilityOf(element))
    }

    fun clickUsingJavaScript(webDriver: ChromeDriver, element: WebElement) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript(CLICK_SCRIPT, element)
    }

    fun waitForFileDownload(driver: WebDriver) {
        switchToDownloadTab(driver)
        val jsExecutor = driver as JavascriptExecutor
        var percentage = 0L
        while (percentage < 100L) {
            try {
                logger.info("Download progress: $percentage%.")
                percentage = getDownloadProgress(jsExecutor)
            } catch (e: Exception) {
                logger.info("Unexpected exception occurred when checking download progress.")
                logger.info(e.message)
                // Nothing to do just wait
            }
            Thread.sleep(WAIT_FOR_DOWNLOAD_CHECK)
        }
    }

    fun isDownloading(webDriver: ChromeDriver): Boolean = getDownloadProgress(webDriver) > 0

    fun switchToDownloadTab(webDriver: WebDriver) {
        for (winHandle in webDriver.windowHandles) {
            webDriver.switchTo().window(winHandle)
        }
        webDriver[CHROME_DOWNLOADS]
    }

    private fun getDownloadProgress(jsExecutor: JavascriptExecutor): Long =
        jsExecutor.executeScript(DOWNLOAD_PROGRESS_VALUE_SCRIPT) as Long
}