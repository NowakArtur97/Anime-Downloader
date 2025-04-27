package com.nowakartur.animedownloader.animeheaven

import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.FileRenamingService
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus
import org.apache.commons.lang3.exception.ExceptionUtils
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AnimeHeavenDownloadService(
    @Value("\${app.animeheaven.url}") private val animeheavenPageUrl: String,
    @Value("\${app.download-directory}") private val downloadDirectory: String,
    @Value("\${app.consumer.download-service-retry-times}") private val downloadServiceRetryTimes: Int,
    private val subscribedAnimeService: SubscribedAnimeService,
    private val fileRenamingService: FileRenamingService,
    private val screenshotUtil: ScreenshotUtil,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadAnime() {

        val subscribedAnime = subscribedAnimeService.findAllAnimeForDownload()

        if (subscribedAnime.isEmpty()) {
            logger.info("Nothing new to download or no anime is subscribed.")
            return
        }

        val allAnimeDownloadInfo = AnimeHeavenPage.getAllAnimeDownloadInfo(animeheavenPageUrl)
        val titles = subscribedAnime.map { it.title }
        val subscribedAnimeDownloadInfo = allAnimeDownloadInfo
            .filter { titles.contains(it.title) }
            .filter { subscribedAnime.find { anime -> anime.title == it.title }!!.episodeNumber < it.episodeNumber }

        subscribedAnimeDownloadInfo.forEach {
            var isDownloading = true
            var hasFailed = false
            var currentRetryIndex = 0
            var webDriver: RemoteWebDriver? = null
            val subscribedAnimeEntity = subscribedAnime.find { anime -> anime.title == it.title }!!

            while (isDownloading) {

                try {

                    val title = subscribedAnimeEntity.title
                    logger.info("Downloading the episode of: [$title].")

                    subscribedAnimeService.startDownloadingAnime(subscribedAnimeEntity)

                    webDriver = SeleniumUtil.startWebDriver(downloadDirectory)
                    AnimeHeavenPage.connectToEpisodePage(webDriver, it.url)
                    AnimeHeavenPage.selectNewestEpisode(webDriver)
                    AnimeHeavenPage.downloadEpisode(webDriver)

                    SeleniumUtil.waitForFileDownload(webDriver, title)

                    subscribedAnimeService.finishDownloadingAnime(subscribedAnimeEntity, it.episodeNumber)

                    logger.info("The [$title] episode has been successfully downloaded.")

                    fileRenamingService.renameNewestEpisodeTo(title, it.episodeNumber)

                    isDownloading = false
                } catch (e: Exception) {
                    logger.error("Unexpected exception occurred when downloading episode of [${it.title}].")
                    logger.error(e.message)
                    logger.error(ExceptionUtils.getStackTrace(e))
                    subscribedAnimeService.waitForDownloadingAnime(subscribedAnimeEntity)
                    screenshotUtil.takeScreenshot(webDriver, it.title)

                    currentRetryIndex++
                    hasFailed = currentRetryIndex >= downloadServiceRetryTimes
                    isDownloading = currentRetryIndex < downloadServiceRetryTimes

                } finally {
                    webDriver?.quit()
                }
            }

            val hasFailedDownload = hasFailed && subscribedAnimeEntity.status != SubscribedAnimeStatus.DOWNLOADED
            if (hasFailedDownload) {
                subscribedAnimeService.setAsFailedAnimeDownload(subscribedAnimeEntity)
            }
        }
    }
}
