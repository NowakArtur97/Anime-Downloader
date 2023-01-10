package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnPage
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
    @Value("\${app.consumer.wait-time-seconds}") private val consumerWaitTime: Long,
    @Value("\${app.consumer.download-service-retry-times}") private val downloadServiceRetryTimes: Int,
    @Value("\${app.supported-servers}") private val supportedServersNames: List<String>,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadAnime() {

        logger.info("Searching for all subscribed anime for download.")

        val subscribedAnime = subscribedAnimeService.findAllAnimeForDownload()

        if (subscribedAnime.isEmpty()) {
            logger.info("Nothing new to download or no anime is subscribed.")
            return
        }

        logger.info("Connecting to gogoanime page.")

        val mainPage: Document = GogoanimeMainPage.connectToMainPage(gogoanimeMainPageUrl)

        val allNewAnimeToDownloadElements: List<Element> =
            GogoanimeMainPage.findAllSubscribedAnime(subscribedAnime, mainPage)

        if (allNewAnimeToDownloadElements.isEmpty()) {
            logger.info("Nothing new to download from gogoanime page.")
            return
        }

        val allNewAnimeToDownload: MutableList<SubscribedAnimeEntity> = subscribedAnime.filter { anime ->
            allNewAnimeToDownloadElements.any {
                it.text().contains(anime.title, ignoreCase = true)
            }
        }.toMutableList()

        logger.info("Anime found: ${allNewAnimeToDownload.map { it.title }}.")

        val allSupportedServers = listOf(Mp4UploadPage, StreamSbPage, XStreamCdnPage)
        val supportedServers = supportedServersNames
            .map { name -> allSupportedServers.find { it.toString().contains(name) }!! }

        val downloadInfoQueue: BlockingQueue<List<DownloadInfo>> = ArrayBlockingQueue(allNewAnimeToDownload.size)

        val gogoanimeDownloadInfoProducer = GogoanimeDownloadInfoProducer(
            subscribedAnimeService,
            screenshotUtil,
            downloadInfoQueue,
            allNewAnimeToDownloadElements,
            allNewAnimeToDownload,
            gogoanimeMainPageUrl,
            supportedServers
        )
        val gogoanimeDownloadInfoConsumer = GogoanimeDownloadInfoConsumer(
            subscribedAnimeService,
            screenshotUtil,
            downloadInfoQueue,
            allNewAnimeToDownload,
            consumerWaitTime,
            downloadServiceRetryTimes
        )

        gogoanimeDownloadInfoProducer.name = "producer-thread"
        gogoanimeDownloadInfoConsumer.name = "consumer-thread"

        gogoanimeDownloadInfoProducer.start()
        gogoanimeDownloadInfoConsumer.start()

        gogoanimeDownloadInfoProducer.join()
        gogoanimeDownloadInfoConsumer.join()
    }
}
