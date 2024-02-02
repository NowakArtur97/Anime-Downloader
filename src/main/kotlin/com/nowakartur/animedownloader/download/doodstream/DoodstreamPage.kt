package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.doodstream.DoodstreamStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.doodstream.DoodstreamStyles.SIZE_TEXT_CLASS_NAME
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object DoodstreamPage : DownloadPage {

    override val episodePageDownloadLinkTexts: List<String> get() = listOf("dood")
    override val episodePageDownloadLinkClass: String get() = "doodstream"

    override fun prepareDownloadLink(page: Document): String =
        getDownloadLink(page, episodePageDownloadLinkClass)

    override fun findFileSize(url: String): Float = Jsoup
        .connect(url.replace("/e/", "/d/"))
        .userAgent("Chrome")
        .get()
        .getElementsByClass(SIZE_TEXT_CLASS_NAME)
        .first()!!
        .text()
        .substringBefore(AFTER_SIZE_TEXT)
        .toFloat()

    override fun downloadEpisode(webDriver: RemoteWebDriver) {
        downloadFromVideo(webDriver)
    }

    private fun downloadFromVideo(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.tagName(HtmlConstants.VIDEO_TAG))
        SeleniumUtil.downloadVideoUsingJavaScript(webDriver)
        SeleniumUtil.downloadVideoUsingJavaScript(webDriver)
    }
}
