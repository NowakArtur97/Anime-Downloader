package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.facade.DownloadFacade
import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.apache.commons.lang3.exception.ExceptionUtils
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue

class GogoanimeDownloadInfoConsumer(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val downloadInfoQueue: BlockingQueue<List<DownloadInfo>>,
    private val allNewAnimeToDownload: List<SubscribedAnimeEntity>,
) : Thread() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run() {

        var downloadCounter = 0

        while (downloadCounter < allNewAnimeToDownload.size) {

            var isDownloading = true
            var currentIndex = 0
            var webDriver: RemoteWebDriver? = null
            var subscribedAnimeEntity: SubscribedAnimeEntity? = null
            var downloadInfo: List<DownloadInfo> = emptyList()

            while (isDownloading) {

                try {
                    downloadInfo = downloadInfoQueue.take()
                    val bestQualityDownloadPage = downloadInfo[currentIndex]
                    subscribedAnimeEntity =
                        allNewAnimeToDownload.find { it.title == downloadInfo.first().title }!! // TODO: Change

                    subscribedAnimeService.startDownloadingAnime(subscribedAnimeEntity)

                    webDriver = SeleniumUtil.startWebDriver()

                    DownloadFacade.downloadInBestQuality(webDriver, bestQualityDownloadPage)

                    SeleniumUtil.waitForFileDownload(webDriver)

                    subscribedAnimeService.finishDownloadingAnime(subscribedAnimeEntity)

                    logger.info("The [${subscribedAnimeEntity.title}] episode has been successfully downloaded.")

                    isDownloading = false

                    downloadCounter++

                } catch (e: Exception) {
                    cleanUpAfterException(e, subscribedAnimeEntity, webDriver)

                    isDownloading = ++currentIndex < downloadInfo.size

                } finally {
                    webDriver?.quit()
                }
            }
        }
    }

    private fun cleanUpAfterException(
        e: Exception,
        subscribedAnimeEntity: SubscribedAnimeEntity?,
        webDriver: RemoteWebDriver?
    ) {
        logger.error("Unexpected exception occurred when downloading episode of [${subscribedAnimeEntity?.title}].")
        logger.info(e.message)
        val stackTrace: String = ExceptionUtils.getStackTrace(e)
        logger.error(stackTrace)

        if (subscribedAnimeEntity != null) {
            subscribedAnimeService.waitForDownload(subscribedAnimeEntity)
        }

//        screenshotUtil.takeScreenshot(webDriver, subscribedAnimeEntity.title)
    }
}
