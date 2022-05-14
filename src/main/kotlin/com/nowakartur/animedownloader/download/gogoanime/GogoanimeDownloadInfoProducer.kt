package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.facade.DownloadFacade
import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.apache.commons.lang3.exception.ExceptionUtils
import org.jsoup.nodes.Element
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue

class GogoanimeDownloadInfoProducer(
    private val subscribedAnimeService: SubscribedAnimeService,
    private val downloadInfoQueue: BlockingQueue<List<DownloadInfo>>,
    private val allNewAnimeToDownloadElements: List<Element>,
    private val allNewAnimeToDownload: List<SubscribedAnimeEntity>
) : Thread() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run() {

        allNewAnimeToDownload.forEach { subscribedAnimeEntity ->
            val linkToAnimePage: String =
                GogoanimeMainPage.findLinkToEpisodes(allNewAnimeToDownloadElements, subscribedAnimeEntity.title)

            logger.info("Connecting to the episode page of: [${subscribedAnimeEntity.title}].")

            val episodePage = GogoanimeEpisodePage.connectToEpisodePage(linkToAnimePage)

            val goloadLink = GogoanimeEpisodePage.findLinkForDownload(episodePage)

            logger.info("Connecting to the download page: [$goloadLink].")

            var webDriver: RemoteWebDriver? = null

            try {
                webDriver = SeleniumUtil.startWebDriver()

                val downloadInfo = DownloadFacade.getDownloadInfo(subscribedAnimeEntity.title, webDriver, goloadLink)

                downloadInfoQueue.offer(downloadInfo)

            } catch (e: Exception) {
                cleanUpAfterException(e, subscribedAnimeEntity, webDriver)
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

//        screenshotUtil.takeScreenshot(webDriver, subscribedAnimeEntity.title)
    }
}
