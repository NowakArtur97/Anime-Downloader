package com.nowakartur.animedownloader.download.xstreamcdn

import com.nowakartur.animedownloader.constant.HtmlConstants.ANCHOR_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.BUTTON_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.H2_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.HREF_ATTRIBUTE
import com.nowakartur.animedownloader.constant.HtmlConstants.VIDEO_TAG
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnStyles.BEFORE_SIZE_TEXT
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnStyles.EPISODE_PAGE_LINK_CLASS
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnStyles.QUALITY_DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object XStreamCdnPage : DownloadPage {

    override val downloadLinkText: String get() = "fembed-hd"

    override fun prepareDownloadLink(page: Document): String =
        getDownloadLink(page, EPISODE_PAGE_LINK_CLASS)
            .replace("/v/", "/f/")

    override fun findFileSize(url: String): Float = Jsoup
        .connect(url)
        .get()
        .getElementsByTag(H2_TAG)
        .first()!!
        .text()
        .substringAfter(BEFORE_SIZE_TEXT)
        .substringBefore(AFTER_SIZE_TEXT)
        .toFloat()

    override fun downloadEpisode(webDriver: RemoteWebDriver) {
        clickFirstDownloadButton(webDriver)
        clickSecondDownloadButton(webDriver)
    }

    private fun clickFirstDownloadButton(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.tagName(BUTTON_TAG))
        val firstDownloadButton = webDriver.findElementByTagName(BUTTON_TAG)
        SeleniumUtil.clickUsingJavaScript(webDriver, firstDownloadButton)
    }

    private fun clickSecondDownloadButton(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(QUALITY_DOWNLOAD_BUTTON_CLASS))
        val bestQualityDownloadLink = webDriver.findElementsByClassName(QUALITY_DOWNLOAD_BUTTON_CLASS)
            .last()
            .findElement(By.tagName(ANCHOR_TAG))
        SeleniumUtil.waitFor(webDriver, bestQualityDownloadLink)
        val bestQualityVideoUrl = bestQualityDownloadLink.getAttribute(HREF_ATTRIBUTE)
        connectToDownloadPage(webDriver, bestQualityVideoUrl)
        SeleniumUtil.waitFor(webDriver, By.tagName(VIDEO_TAG))
        SeleniumUtil.downloadVideoUsingJavaScript(webDriver)
    }
}
