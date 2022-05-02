package com.nowakartur.animedownloader.download.common

import org.openqa.selenium.chrome.ChromeDriver

interface DownloadPage {

    fun connectToDownloadPage(webDriver: ChromeDriver, url: String) {
        webDriver.get(url)
    }

    fun downloadEpisode(webDriver: ChromeDriver)

    fun findFileSize(url: String): Float
}
