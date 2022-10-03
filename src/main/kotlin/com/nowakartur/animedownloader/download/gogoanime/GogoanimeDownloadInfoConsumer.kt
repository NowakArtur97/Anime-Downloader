package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.openqa.selenium.remote.RemoteWebDriver
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

class GogoanimeDownloadInfoConsumer(
    subscribedAnimeService: SubscribedAnimeService,
    screenshotUtil: ScreenshotUtil,

    private val downloadInfoQueue: BlockingQueue<List<DownloadInfo>>,
    private val allNewAnimeToDownload: List<SubscribedAnimeEntity>,
    private val consumerWaitTime: Long,
    private val downloadServiceRetryTimes: Int,
) : GogoanimeDownloadInfoThread(subscribedAnimeService, screenshotUtil) {

    override fun run() {

        var downloadCounter = 0

        while (downloadCounter < allNewAnimeToDownload.size && allNewAnimeToDownload.isNotEmpty()) {

            var isDownloading = true
            var currentDownloadServiceIndex = 0
            var webDriver: RemoteWebDriver? = null

            val downloadInfo = downloadInfoQueue.poll(consumerWaitTime, TimeUnit.SECONDS)

            if (downloadInfo.isNullOrEmpty()) {
                return
            }

            while (isDownloading) {

                val bestQualityDownloadPage = downloadInfo[currentDownloadServiceIndex]
                val subscribedAnimeEntity = allNewAnimeToDownload.first { it.title == downloadInfo.first().title }
                var currentRetryIndex = 0

                while (currentRetryIndex < downloadServiceRetryTimes) {

                    try {

                        subscribedAnimeService.startDownloadingAnime(subscribedAnimeEntity)

                        webDriver = SeleniumUtil.startWebDriver()

                        logger.info("Link to the episode: [${bestQualityDownloadPage.url}].")

                        bestQualityDownloadPage.downloadPage.connectToDownloadPage(
                            webDriver,
                            bestQualityDownloadPage.url
                        )

                        val title = subscribedAnimeEntity.title
                        logger.info("Downloading the episode of: [$title].")

                        bestQualityDownloadPage.downloadPage.downloadEpisode(webDriver)

                        SeleniumUtil.waitForFileDownload(webDriver, title)

                        subscribedAnimeService.finishDownloadingAnime(subscribedAnimeEntity)

                        logger.info("The [$title] episode has been successfully downloaded.")

                        isDownloading = false

                        currentRetryIndex = downloadServiceRetryTimes

                        downloadCounter++

                    } catch (e: Exception) {
                        cleanUpAfterException(e, subscribedAnimeEntity, webDriver)

                        currentRetryIndex++
                        isDownloading = currentDownloadServiceIndex < downloadInfo.size

                    } finally {
                        webDriver?.quit()
                    }
                }

                currentDownloadServiceIndex++
            }
        }
    }
}
