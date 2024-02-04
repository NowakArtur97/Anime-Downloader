package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.FileRenamingService
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.openqa.selenium.remote.RemoteWebDriver
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

class GogoanimeDownloadInfoConsumer(
    subscribedAnimeService: SubscribedAnimeService,
    screenshotUtil: ScreenshotUtil,
    private val fileRenamingService: FileRenamingService,

    private val downloadInfoQueue: BlockingQueue<List<DownloadInfo>>,
    private val allNewAnimeToDownload: List<SubscribedAnimeEntity>,
    private val consumerWaitTime: Long,
    private val downloadServiceRetryTimes: Int,
    private val downloadDirectory: String,
) : GogoanimeDownloadInfoThread(subscribedAnimeService, screenshotUtil) {

    override fun run() {

        var downloadCounter = 0
        var hasFailed = false

        while (downloadCounter < allNewAnimeToDownload.size && allNewAnimeToDownload.isNotEmpty()) {

            var isDownloading = true
            var currentDownloadServiceIndex = 0
            var webDriver: RemoteWebDriver? = null

            val downloadInfo = chooseElementIncludingSkipped(hasFailed, downloadCounter)
            hasFailed = false

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

                        webDriver = SeleniumUtil.startWebDriver(downloadDirectory)

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

                        fileRenamingService.renameNewestEpisodeTo(
                            subscribedAnimeEntity.title,
                            bestQualityDownloadPage.episodeNumber
                        )

                        isDownloading = false

                        currentRetryIndex = downloadServiceRetryTimes

                        downloadCounter++

                    } catch (e: Exception) {
                        cleanUpAfterException(e, subscribedAnimeEntity, webDriver)

                        currentRetryIndex++
                        hasFailed = currentRetryIndex >= downloadServiceRetryTimes
                        isDownloading = currentDownloadServiceIndex < downloadInfo.size - 1

                    } finally {
                        webDriver?.quit()
                    }
                }

                currentDownloadServiceIndex++

                val hasFailedDownloadOnAllServers = !isDownloading && hasFailed
                if (hasFailedDownloadOnAllServers) {
                    subscribedAnimeService.setAsFailedAnimeDownload(subscribedAnimeEntity)
                }
            }
        }
    }

    private fun chooseElementIncludingSkipped(
        hasFailed: Boolean,
        downloadCounter: Int
    ) = if (hasFailed) {
        if (downloadInfoQueue.size <= 1) {
            downloadInfoQueue.poll(consumerWaitTime, TimeUnit.SECONDS)
        } else {
            val indexToSkipFailedElement = downloadCounter + 1
            val downloadInfos = downloadInfoQueue.toList()[indexToSkipFailedElement]
            downloadInfoQueue.remove(downloadInfos)
            downloadInfos
        }
    } else {
        downloadInfoQueue.poll(consumerWaitTime, TimeUnit.SECONDS)
    }
}
