package com.nowakartur.animedownloader.download.mp4upload

import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.BEFORE_SIZE_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.DOWNLOAD_PAGE_SUBMIT_BUTTON_ID
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.FILE_SIZE_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object Mp4UploadPage : DownloadPage {

    override val episodePageDownloadLinkTexts: List<String> get() = listOf("mp4upload")
    override val episodePageDownloadLinkClass: String get() = "mp4upload"

    private const val WAIT_TIME_FOR_BUTTON: Long = 35

    override fun prepareDownloadLink(page: Document): String =
        getDownloadLink(page, episodePageDownloadLinkClass)
            .replace("embed-", "")

    override fun findFileSize(url: String): Float = Jsoup
        .connect(url)
        .get()
        .getElementsByClass(FILE_SIZE_CLASS).first()!!
        .children()[2]
        .text()
        .substringAfter(BEFORE_SIZE_TEXT)
        .substringBefore(AFTER_SIZE_TEXT)
        .toFloat()

    override fun downloadEpisode(webDriver: RemoteWebDriver) {
        clickGoToDownloadPageButton(webDriver)
        clickDownloadButton(webDriver)
    }

    private fun clickGoToDownloadPageButton(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.id(DOWNLOAD_PAGE_SUBMIT_BUTTON_ID))
        val downloadPageRedirectButton = webDriver.findElementById(DOWNLOAD_PAGE_SUBMIT_BUTTON_ID)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadPageRedirectButton)
    }

    private fun clickDownloadButton(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_BUTTON_CLASS), WAIT_TIME_FOR_BUTTON)
        val downloadButton = webDriver.findElementByClassName(DOWNLOAD_BUTTON_CLASS)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadButton)
    }
}
