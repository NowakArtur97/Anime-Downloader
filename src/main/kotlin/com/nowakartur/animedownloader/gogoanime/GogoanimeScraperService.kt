package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.goland.GolandDownloadPage
import com.nowakartur.animedownloader.m4upload.M4UploadPage
import io.github.bonigarcia.wdm.WebDriverManager
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.openqa.selenium.chrome.ChromeDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GogoanimeScraperService(
    private val gogoanimeMainPage: GogoanimeMainPage,
    private val gogoanimeEpisodePage: GogoanimeEpisodePage,
    private val golandDownloadPage: GolandDownloadPage,
    private val m4UploadPage: M4UploadPage,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun scrapDownloadLinks(subscribedAnime: List<String>) {

        logger.info("Connecting to gogoanime page.")

        val mainPage: Document = gogoanimeMainPage.connectToMainPage()

        val allSubscribedAnime: List<Element> =
            gogoanimeMainPage.findAllSubscribedAnime(subscribedAnime, mainPage)

        logger.info("Anime found: ${allSubscribedAnime.map { it.text() }}.")

        val allLinksToAnimePages: List<String> =
            gogoanimeMainPage.findAllLinksToEpisodes(allSubscribedAnime, mainPage)

        WebDriverManager.chromedriver().setup()
        val webDriver = ChromeDriver()

        allLinksToAnimePages.map {

            logger.info("Connecting to episode page: $it.")

            val episodePage = gogoanimeEpisodePage.connectToEpisodePage(it)

            gogoanimeEpisodePage.findLinkForDownload(episodePage)

        }.forEach() { downloadLink ->

            logger.info("Connecting to download page: $downloadLink.")

            golandDownloadPage.connectToGolandPage(webDriver, downloadLink)

            val m4UploadDownloadLink = golandDownloadPage.findM4UploadDownloadLink(webDriver)!!

            logger.info("Link for M4Upload: $m4UploadDownloadLink.")

            m4UploadPage.connectToDownloadPage(webDriver, m4UploadDownloadLink)

            logger.info("Download file from M4Upload.")

            m4UploadPage.downloadEpisode(webDriver)

            webDriver.quit()
        }
    }
}
