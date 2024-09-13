package com.nowakartur.animedownloader.download.mp4upload

import com.nowakartur.animedownloader.constant.HtmlConstants.VIDEO_TAG
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.BEFORE_SIZE_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadStyles.FILE_SIZE_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object Mp4UploadPage : DownloadPage {

    private const val WAIT_TIME_BEFORE_DOWNLOAD = 5L

    override val episodePageDownloadLinkTexts: List<String> get() = listOf("mp4upload")
    override val episodePageDownloadLinkClass: String get() = "mp4upload"

    override fun prepareDownloadLink(page: Document): String = getDownloadLink(page, episodePageDownloadLinkClass)

    override fun findFileSize(url: String): Float = Jsoup
        .connect(url.replace("embed-", ""))
        .get()
        .getElementsByClass(FILE_SIZE_CLASS).first()!!
        .children()[2]
        .text()
        .substringAfter(BEFORE_SIZE_TEXT)
        .substringBefore(AFTER_SIZE_TEXT)
        .toFloat()

    override fun downloadEpisode(webDriver: RemoteWebDriver) {
        downloadFromVideo(webDriver)
    }

    private fun downloadFromVideo(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.tagName(VIDEO_TAG))
        SeleniumUtil.waitFor(WAIT_TIME_BEFORE_DOWNLOAD)
        SeleniumUtil.downloadVideoUsingJavaScript(webDriver)
    }
}
