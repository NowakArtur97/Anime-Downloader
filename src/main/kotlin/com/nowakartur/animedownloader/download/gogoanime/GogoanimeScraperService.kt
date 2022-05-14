package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


@Service
class GogoanimeScraperService(
    private val subscribedAnimeService: SubscribedAnimeService,
//    private val gogoanimeMainPage: GogoanimeMainPage,
//    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val screenshotUtil: ScreenshotUtil,
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

        val mainPage: Document = GogoanimeMainPage.connectToMainPage()

        val allNewAnimeToDownloadElements: List<Element> =
            GogoanimeMainPage.findAllSubscribedAnime(subscribedAnime, mainPage)

        if (allNewAnimeToDownloadElements.isEmpty()) {
            logger.info("Nothing new to download.")
            return
        }

        val allNewAnimeToDownload: List<SubscribedAnimeEntity> =
            subscribedAnime.filter { anime -> allNewAnimeToDownloadElements.any { it.text().contains(anime.title) } }

        logger.info("Anime found: ${allNewAnimeToDownload.map { it.title }}.")

        val downloadInfoQueue: BlockingQueue<List<DownloadInfo>> = ArrayBlockingQueue(allNewAnimeToDownload.size)

        val gogoanimeDownloadInfoProducer = GogoanimeDownloadInfoProducer(
            subscribedAnimeService,
            downloadInfoQueue,
            allNewAnimeToDownloadElements,
            allNewAnimeToDownload
        )
        val gogoanimeDownloadInfoConsumer = GogoanimeDownloadInfoConsumer(
            subscribedAnimeService,
            downloadInfoQueue,
            allNewAnimeToDownload
        )
        gogoanimeDownloadInfoProducer.name = "producer thread"
        gogoanimeDownloadInfoConsumer.name = "consumer thread"

        gogoanimeDownloadInfoProducer.start()
        gogoanimeDownloadInfoConsumer.start()

        gogoanimeDownloadInfoProducer.join()
        gogoanimeDownloadInfoConsumer.join()
    }

//    private fun cleanUpAfterException(
//        e: Exception,
//        subscribedAnimeEntity: SubscribedAnimeEntity,
//        webDriver: RemoteWebDriver?
//    ) {
//        logger.error("Unexpected exception occurred when downloading episode of [${subscribedAnimeEntity.title}].")
//        logger.info(e.message)
//        val stackTrace: String = ExceptionUtils.getStackTrace(e)
//        logger.error(stackTrace)
//
//        subscribedAnimeService.waitForDownload(subscribedAnimeEntity)
//
//        screenshotUtil.takeScreenshot(webDriver, subscribedAnimeEntity.title)
//    }
}
