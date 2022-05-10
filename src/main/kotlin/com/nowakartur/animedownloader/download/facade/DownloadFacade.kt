package com.nowakartur.animedownloader.download.facade

import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory

object DownloadFacade {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadInBestQuality(webDriver: RemoteWebDriver, bestQualityDownloadPage: DownloadInfo) {

        logger.info("Link to the episode: [${bestQualityDownloadPage.url}].")
        bestQualityDownloadPage.downloadPage.connectToDownloadPage(webDriver, bestQualityDownloadPage.url)
        logger.info("Downloading the episode.")
        bestQualityDownloadPage.downloadPage.downloadEpisode(webDriver)
    }
}
