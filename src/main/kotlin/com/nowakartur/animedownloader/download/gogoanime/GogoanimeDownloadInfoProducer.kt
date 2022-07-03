package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeEpisodePage.mapToDownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.jsoup.nodes.Element
import java.util.concurrent.BlockingQueue

class GogoanimeDownloadInfoProducer(
    subscribedAnimeService: SubscribedAnimeService,
    screenshotUtil: ScreenshotUtil,

    private val downloadInfoQueue: BlockingQueue<List<DownloadInfo>>,
    private val allNewAnimeToDownloadElements: List<Element>,
    private val allNewAnimeToDownload: List<SubscribedAnimeEntity>,
    private val gogoanimeMainPageUrl: String,
) : GogoanimeDownloadInfoThread(subscribedAnimeService, screenshotUtil) {

    override fun run() {

        allNewAnimeToDownload.forEach { subscribedAnimeEntity ->
            val linkToEpisodePage: String =
                GogoanimeMainPage.findLinkToEpisodes(allNewAnimeToDownloadElements, subscribedAnimeEntity.title)

            logger.info("Connecting to the episode page of: [${subscribedAnimeEntity.title}].")

            val episodePage = GogoanimeEpisodePage.connectToEpisodePage(gogoanimeMainPageUrl, linkToEpisodePage)

            val alSupportedDownloadLinks = GogoanimeEpisodePage.findAllSupportedDownloadLinks(episodePage)

            val downloadInfo = mapToDownloadInfo(subscribedAnimeEntity.title, alSupportedDownloadLinks)
                .sortedByDescending { it.fileSize }

            downloadInfoQueue.add(downloadInfo)
        }
    }
}
