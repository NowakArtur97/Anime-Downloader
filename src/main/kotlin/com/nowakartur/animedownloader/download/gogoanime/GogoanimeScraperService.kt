package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.facade.DownloadFacade
import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.apache.commons.lang3.exception.ExceptionUtils
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GogoanimeScraperService(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val screenshotUtil: ScreenshotUtil
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadAnime() {

        logger.info("Searching for all subscribed anime for download.")

        val subscribedAnime = subscribedAnimeService.findAllAnimeForDownload()

        if (subscribedAnime.isEmpty()) {
            logger.info("No anime is subscribed.")
            return
        }

        logger.info("Connecting to gogoanime page.")

        val mainPage: Document = gogoanimeMainPage.connectToMainPage()

        val allNewAnimeToDownloadElements: List<Element> =
            gogoanimeMainPage.findAllSubscribedAnime(subscribedAnime, mainPage)

        if (allNewAnimeToDownloadElements.isEmpty()) {
            logger.info("Nothing new to download.")
            return
        }

        val allNewAnimeToDownload: List<SubscribedAnimeEntity> =
            subscribedAnime.filter { anime -> allNewAnimeToDownloadElements.any { it.text().contains(anime.title) } }

        logger.info("Anime found: ${allNewAnimeToDownload.map { it.title }}.")

        allNewAnimeToDownload.forEach { subscribedAnimeEntity ->
            val linkToAnimePage: String =
                gogoanimeMainPage.findLinkToEpisodes(allNewAnimeToDownloadElements, subscribedAnimeEntity.title)

            logger.info("Connecting to the episode page of: [${subscribedAnimeEntity.title}].")

            val episodePage = gogoanimeEpisodePage.connectToEpisodePage(linkToAnimePage)

            val goloadLink = gogoanimeEpisodePage.findLinkForDownload(episodePage)

            logger.info("Connecting to the download page: [$goloadLink].")

            var webDriver: RemoteWebDriver? = null
            var downloadInfo: List<DownloadInfo> = emptyList()

            try {
                webDriver = SeleniumUtil.startWebDriver()

                downloadInfo = DownloadFacade.getDownloadInfo(webDriver, goloadLink)

            } catch (e: Exception) {
                cleanUpAfterException(e, subscribedAnimeEntity, webDriver)
            } finally {
                webDriver?.quit()
            }

            if (downloadInfo.isEmpty()) {
                logger.info("No supported services were found.")
            } else {
                downloadEpisode(downloadInfo, subscribedAnimeEntity)
            }
        }
    }

    private fun downloadEpisode(
        downloadInfo: List<DownloadInfo>,
        subscribedAnimeEntity: SubscribedAnimeEntity
    ) {
        var isDownloading = true
        var currentIndex = 0
        var webDriver: RemoteWebDriver? = null

        while (isDownloading) {

            try {
                val bestQualityDownloadPage = downloadInfo[currentIndex]

                subscribedAnimeService.startDownloadingAnime(subscribedAnimeEntity)

                webDriver = SeleniumUtil.startWebDriver()

                DownloadFacade.downloadInBestQuality(webDriver, bestQualityDownloadPage)

                SeleniumUtil.waitForFileDownload(webDriver)

                subscribedAnimeService.finishDownloadingAnime(subscribedAnimeEntity)

                logger.info("The [${subscribedAnimeEntity.title}] episode has been successfully downloaded.")

                isDownloading = false

            } catch (e: Exception) {
                cleanUpAfterException(e, subscribedAnimeEntity, webDriver)

                isDownloading = ++currentIndex < downloadInfo.size

            } finally {
                webDriver?.quit()
            }
        }
    }

    private fun cleanUpAfterException(
        e: Exception,
        subscribedAnimeEntity: SubscribedAnimeEntity,
        webDriver: RemoteWebDriver?
    ) {
        logger.error("Unexpected exception occurred when downloading episode of [${subscribedAnimeEntity.title}].")
        logger.info(e.message)
        val stackTrace: String = ExceptionUtils.getStackTrace(e)
        logger.error(stackTrace)

        subscribedAnimeService.waitForDownload(subscribedAnimeEntity)

        screenshotUtil.takeScreenshot(webDriver, subscribedAnimeEntity.title)
    }
}
