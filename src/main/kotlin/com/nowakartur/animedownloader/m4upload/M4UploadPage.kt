package com.nowakartur.animedownloader.m4upload

import com.nowakartur.animedownloader.util.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.stereotype.Service

@Service
class M4UploadPage {

    fun connectToDownloadPage(webDriver: ChromeDriver, url: String) {
        webDriver.get(url)
    }

    fun downloadEpisode(webDriver: ChromeDriver) {
        clickGoToDownloadPageButton(webDriver)
        clickDownloadButton(webDriver)
    }

    private fun clickGoToDownloadPageButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.id(M4UploadStyles.DOWNLOAD_PAGE_SUBMIT_BUTTON_ID))
        SeleniumUtil.clickUsingJavaScript(
            webDriver,
            webDriver.findElementById(M4UploadStyles.DOWNLOAD_PAGE_SUBMIT_BUTTON_ID)
        )
    }

    private fun clickDownloadButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(M4UploadStyles.DOWNLOAD_BUTTON_CLASS))
        webDriver.findElementByClassName(M4UploadStyles.DOWNLOAD_BUTTON_CLASS).click()
    }
}
