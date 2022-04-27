package com.nowakartur.animedownloader.util

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

private const val WAIT_TIMEOUT = 15L

object SeleniumUtil {

    fun waitFor(webDriver: ChromeDriver, by: By) {
        val wait = WebDriverWait(webDriver, WAIT_TIMEOUT)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by))
    }

    fun clickUsingJavaScript(webDriver: ChromeDriver, element: WebElement) {
        val executor = webDriver as JavascriptExecutor
        executor.executeScript("arguments[0].click();", element)
    }
}
