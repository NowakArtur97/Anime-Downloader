package com.nowakartur.animedownloader.download.facade

import com.nowakartur.animedownloader.download.goload.GoloadDownloadPage
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory

object DownloadFacade {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getDownloadInfo(title: String, webDriver: RemoteWebDriver, goloadLink: String): List<DownloadInfo> {

        GoloadDownloadPage.connectToGolandPage(webDriver, goloadLink)

        val allSupportedDownloadLinks = GoloadDownloadPage.findAllSupportedDownloadLinks(webDriver)

        return GoloadDownloadPage.mapToDownloadInfo(title, allSupportedDownloadLinks)
            .sortedByDescending { it.fileSize }
    }

    fun downloadInBestQuality(webDriver: RemoteWebDriver, bestQualityDownloadPage: DownloadInfo) {
        logger.info("Link to the episode: [${bestQualityDownloadPage.url}].")

        bestQualityDownloadPage.downloadPage.connectToDownloadPage(webDriver, bestQualityDownloadPage.url)

        logger.info("Downloading the episode.")

        bestQualityDownloadPage.downloadPage.downloadEpisode(webDriver)
    }
}
