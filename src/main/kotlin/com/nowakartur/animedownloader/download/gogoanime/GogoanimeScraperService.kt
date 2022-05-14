package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

@Service
class GogoanimeScraperService(
    @Value("\${app.gogoanime.url}") private val gogoanimeMainPageUrl: String,
    private val subscribedAnimeService: SubscribedAnimeService,
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

        val mainPage: Document = GogoanimeMainPage.connectToMainPage(gogoanimeMainPageUrl)

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
            screenshotUtil,
            downloadInfoQueue,
            allNewAnimeToDownloadElements,
            allNewAnimeToDownload,
            gogoanimeMainPageUrl,
        )
        val gogoanimeDownloadInfoConsumer = GogoanimeDownloadInfoConsumer(
            subscribedAnimeService,
            screenshotUtil,
            downloadInfoQueue,
            allNewAnimeToDownload,
        )
        gogoanimeDownloadInfoProducer.name = "producer thread"
        gogoanimeDownloadInfoConsumer.name = "consumer thread"

        gogoanimeDownloadInfoProducer.start()
        gogoanimeDownloadInfoConsumer.start()

        gogoanimeDownloadInfoProducer.join()
        gogoanimeDownloadInfoConsumer.join()
    }
}
