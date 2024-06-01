package com.nowakartur.animedownloader.selenium

import com.nowakartur.animedownloader.exception.WrongFileException
import com.nowakartur.animedownloader.selenium.JsScripts.CLICK_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.DOWNLOAD_PROGRESS_VALUE_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.DOWNLOAD_VIDEO_FROM_SOURCE_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.DOWNLOAD_VIDEO_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.FILE_SIZE_VALUE_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.HAS_DOWNLOAD_STOPPED_DOWNLOAD_SCRIPT
import com.nowakartur.animedownloader.selenium.JsScripts.RESUME_DOWNLOAD_SCRIPT
import io.github.bonigarcia.wdm.WebDriverManager
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object SeleniumUtil {

    private const val WAIT_TIMEOUT_FOR_ELEMENT = 15L
    private const val WAIT_TIMEOUT_BEFORE_SWITCHING_TO_DOWNLOAD_TAB = 5L
    private const val WAIT_FOR_DOWNLOAD_CHECK = 12_000L
    private const val CHROME_DOWNLOADS = "chrome://downloads"
    private const val BEFORE_SIZE_TEXT = "MB of "
    private const val AFTER_SIZE_TEXT = " MB,"
    private const val MIN_FILE_SIZE_ON_DOWNLOAD_PAGE = 10.0
    private const val MAX_NUMBER_OF_EXCEPTIONS = 3
    private val HIDDEN_POSITION = Point(-1800, 0)

    private val logger = LoggerFactory.getLogger(javaClass)

    fun startWebDriver(downloadDirectory: String = ""): RemoteWebDriver {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions().also {
            it.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking")) // disable all popups
            it.addArguments("lang=en-GB") // change language to English
        }
        if (StringUtils.isNotBlank(downloadDirectory)) {
            val chromePrefs = HashMap<String, String>()
            chromePrefs["download.default_directory"] = downloadDirectory
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
        }
        return ChromeDriver(chromeOptions).also {
            it.manage().window().position = HIDDEN_POSITION
        }
    }

    fun waitFor(webDriver: RemoteWebDriver, by: By, timeOutInSeconds: Long = WAIT_TIMEOUT_FOR_ELEMENT) {
        val wait = WebDriverWait(webDriver, timeOutInSeconds)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by))
    }

    fun waitFor(webDriver: RemoteWebDriver, element: WebElement, timeOutInSeconds: Long = WAIT_TIMEOUT_FOR_ELEMENT) {
        val wait = WebDriverWait(webDriver, timeOutInSeconds)
        wait.until(ExpectedConditions.visibilityOf(element))
    }

    private fun waitFor(webDriver: WebDriver, seconds: Long) {
        webDriver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS)
    }

    fun clickUsingJavaScript(webDriver: RemoteWebDriver, element: WebElement) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript(CLICK_SCRIPT, element)
    }

    fun downloadVideoUsingJavaScript(webDriver: RemoteWebDriver) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript(DOWNLOAD_VIDEO_SCRIPT.trimMargin())
    }

    fun downloadVideoFromSourceUsingJavaScript(webDriver: RemoteWebDriver) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript(DOWNLOAD_VIDEO_FROM_SOURCE_SCRIPT.trimMargin())
    }

    fun waitForFileDownload(driver: WebDriver, title: String) {
        waitFor(driver, WAIT_TIMEOUT_BEFORE_SWITCHING_TO_DOWNLOAD_TAB)
        switchToDownloadTab(driver)
        val jsExecutor = driver as JavascriptExecutor
        doubleCheckFileSize(jsExecutor)
        var percentage = 0L
        var exceptionsCounter = 0
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
                exceptionsCounter++
                logger.info("Unexpected exception occurred when checking download progress of: [$title].")
                logger.info(e.message)
                if (exceptionsCounter >= MAX_NUMBER_OF_EXCEPTIONS) {
                    throw e
                }
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

    private fun doubleCheckFileSize(jsExecutor: JavascriptExecutor) {
        val fileSizeOnDownloadPage = jsExecutor.executeScript(FILE_SIZE_VALUE_SCRIPT)
                as String
        val fileSize = fileSizeOnDownloadPage
            .substringAfter(BEFORE_SIZE_TEXT)
            .substringBefore(AFTER_SIZE_TEXT)
            .toFloat()
        if (fileSize < MIN_FILE_SIZE_ON_DOWNLOAD_PAGE) {
            throw WrongFileException("The file size is too small on the download page. Size: [$fileSize]")
        }
    }
}
