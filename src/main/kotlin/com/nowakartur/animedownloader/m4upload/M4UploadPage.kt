package com.nowakartur.animedownloader.m4upload

import com.nowakartur.animedownloader.m4upload.M4UploadStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.m4upload.M4UploadStyles.DOWNLOAD_PAGE_SUBMIT_BUTTON_ID
import com.nowakartur.animedownloader.selenium.SeleniumUtil
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
        SeleniumUtil.waitFor(webDriver, By.id(DOWNLOAD_PAGE_SUBMIT_BUTTON_ID))
        SeleniumUtil.clickUsingJavaScript(
            webDriver,
            webDriver.findElementById(DOWNLOAD_PAGE_SUBMIT_BUTTON_ID)
        )
    }

    private fun clickDownloadButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_BUTTON_CLASS))
        webDriver.findElementByClassName(DOWNLOAD_BUTTON_CLASS).click()
    }
}
