package com.nowakartur.animedownloader.download.m4upload

import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.m4upload.M4UploadStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.download.m4upload.M4UploadStyles.DOWNLOAD_PAGE_SUBMIT_BUTTON_ID
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.stereotype.Component

@Component
class M4UploadPage : DownloadPage {

    override fun downloadEpisode(webDriver: ChromeDriver) {
        clickGoToDownloadPageButton(webDriver)
        clickDownloadButton(webDriver)
    }

    private fun clickGoToDownloadPageButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.id(DOWNLOAD_PAGE_SUBMIT_BUTTON_ID))
        val downloadPageRedirectButton = webDriver.findElementById(DOWNLOAD_PAGE_SUBMIT_BUTTON_ID)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadPageRedirectButton)
    }

    private fun clickDownloadButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_BUTTON_CLASS))
        val downloadButton = webDriver.findElementByClassName(DOWNLOAD_BUTTON_CLASS)
        downloadButton.click()
    }
}
