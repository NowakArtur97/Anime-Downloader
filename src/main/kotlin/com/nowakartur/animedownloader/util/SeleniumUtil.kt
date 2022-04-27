package com.nowakartur.animedownloader.util

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory

private const val WAIT_TIMEOUT_FOR_ELEMENT = 15L
private const val WAIT_FOR_DOWNLOAD_CHECK = 5_000L

object SeleniumUtil {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun startWebDriver(): ChromeDriver {
        WebDriverManager.chromedriver().setup()
        return ChromeDriver()
    }

    fun waitFor(webDriver: ChromeDriver, by: By) {
        val wait = WebDriverWait(webDriver, WAIT_TIMEOUT_FOR_ELEMENT)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by))
    }

    fun clickUsingJavaScript(webDriver: ChromeDriver, element: WebElement) {
        val jsExecutor = webDriver as JavascriptExecutor
        jsExecutor.executeScript("arguments[0].click();", element)
    }

    fun waitForFileDownload(driver: WebDriver) {
        for (winHandle in driver.windowHandles) {
            driver.switchTo().window(winHandle)
        }
        driver["chrome://downloads"]
        val jsExecutor = driver as JavascriptExecutor
        var percentage = 0L
        while (percentage != 100L) {
            try {
                logger.info("Download progress: $percentage/100.")
                percentage = getDownloadProgress(jsExecutor)
            } catch (e: Exception) {
                logger.info(e.message)
                // Nothing to do just wait
            }
            Thread.sleep(WAIT_FOR_DOWNLOAD_CHECK)
        }
    }

    private fun getDownloadProgress(jsExecutor: JavascriptExecutor) =
        jsExecutor.executeScript("return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#progress').value") as Long
}
