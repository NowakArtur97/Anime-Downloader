package com.nowakartur.animedownloader.download.common

import org.openqa.selenium.remote.RemoteWebDriver

interface DownloadPage {

    fun connectToDownloadPage(webDriver: RemoteWebDriver, url: String) {
        webDriver.close()
        webDriver.switchTo().window(webDriver.windowHandles.first())
        webDriver.get(url)
    }

    fun downloadEpisode(webDriver: RemoteWebDriver)

    fun findFileSize(url: String): Float
}
