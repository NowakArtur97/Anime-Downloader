package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.common.DownloadPage
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
    private val supportedServers: List<DownloadPage>
) : GogoanimeDownloadInfoThread(subscribedAnimeService, screenshotUtil) {

    override fun run() {

        allNewAnimeToDownload.forEach { subscribedAnimeEntity ->
            try {
                val linkToEpisodePage: String =
                    GogoanimeMainPage.findLinkToEpisodeByTitle(
                        allNewAnimeToDownloadElements,
                        subscribedAnimeEntity.title
                    )

                logger.info("Connecting to the episode page of: [${subscribedAnimeEntity.title}].")

                val episodePage = GogoanimeEpisodePage.connectToEpisodePage(gogoanimeMainPageUrl, linkToEpisodePage)

                val alSupportedDownloadLinks =
                    GogoanimeEpisodePage.findAllSupportedDownloadLinks(episodePage, supportedServers)

                val episodeNumber = GogoanimeEpisodePage.findEpisodeNumber(episodePage)

                val downloadInfo =
                    GogoanimeEpisodePage.mapToDownloadInfo(
                        subscribedAnimeEntity,
                        alSupportedDownloadLinks,
                        supportedServers,
                        episodeNumber
                    ).sortedByDescending { it.fileSize }

                if (downloadInfo.isNotEmpty()) {
                    downloadInfoQueue.add(downloadInfo)
                } else {
                    logger.info("No service supports episode download of: [${subscribedAnimeEntity.title}].")
                }
            } catch (e: Exception) {
                cleanUpAfterException(e, subscribedAnimeEntity)
            }
        }
    }
}
