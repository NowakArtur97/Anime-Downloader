package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.openqa.selenium.remote.RemoteWebDriver
import java.util.concurrent.BlockingQueue

class GogoanimeDownloadInfoConsumer(
    subscribedAnimeService: SubscribedAnimeService,
    screenshotUtil: ScreenshotUtil,

    private val downloadInfoQueue: BlockingQueue<List<DownloadInfo>>,
    private val allNewAnimeToDownload: List<SubscribedAnimeEntity>,
) : GogoanimeDownloadInfoThread(subscribedAnimeService, screenshotUtil) {

    fun run2(downloadInfo: List<DownloadInfo>) {

        var downloadCounter = 0

        while (downloadCounter < allNewAnimeToDownload.size) {

            var isDownloading = true
            var currentIndex = 0
            var webDriver: RemoteWebDriver? = null

            if (downloadInfo.isEmpty()) {
                return
            }

            val bestQualityDownloadPage = downloadInfo[currentIndex]
            val subscribedAnimeEntity =
                allNewAnimeToDownload.first { it.title == downloadInfo.first().title }

            while (isDownloading) {

                try {

                    subscribedAnimeService.startDownloadingAnime(subscribedAnimeEntity)

                    webDriver = SeleniumUtil.startWebDriver()

                    logger.info("Link to the episode: [${bestQualityDownloadPage.url}].")

                    bestQualityDownloadPage.downloadPage.connectToDownloadPage(webDriver, bestQualityDownloadPage.url)

                    logger.info("Downloading the episode.")

                    bestQualityDownloadPage.downloadPage.downloadEpisode(webDriver)

                    SeleniumUtil.waitForFileDownload(webDriver, subscribedAnimeEntity.title)

                    subscribedAnimeService.finishDownloadingAnime(subscribedAnimeEntity)

                    logger.info("The [${subscribedAnimeEntity.title}] episode has been successfully downloaded.")

                    isDownloading = false

                    downloadCounter++

                } catch (e: Exception) {
                    cleanUpAfterException(e, subscribedAnimeEntity, webDriver)

                    currentIndex++
                    isDownloading = currentIndex < downloadInfo.size

                } finally {
                    webDriver?.quit()
                }
            }
        }
    }
}
