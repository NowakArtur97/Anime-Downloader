package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.facade.DownloadFacade
import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.jsoup.nodes.Element
import org.openqa.selenium.remote.RemoteWebDriver
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
            val linkToAnimePage: String =
                GogoanimeMainPage.findLinkToEpisodes(allNewAnimeToDownloadElements, subscribedAnimeEntity.title)

            logger.info("Connecting to the episode page of: [${subscribedAnimeEntity.title}].")

            val episodePage = GogoanimeEpisodePage.connectToEpisodePage(gogoanimeMainPageUrl, linkToAnimePage)

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
}
