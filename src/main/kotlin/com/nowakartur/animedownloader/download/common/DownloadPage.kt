package com.nowakartur.animedownloader.download.common

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles
import org.jsoup.nodes.Document
import org.openqa.selenium.remote.RemoteWebDriver

interface DownloadPage {

    val episodePageDownloadLinkTexts: List<String>
    val episodePageDownloadLinkClass: String

    fun connectToDownloadPage(webDriver: RemoteWebDriver, url: String) {
        webDriver.close()
        webDriver.switchTo().window(webDriver.windowHandles.first())
        webDriver.get(url)
    }

    fun getDownloadLink(page: Document, pageLinkClass: String): String =
        page.getElementsByClass(pageLinkClass)
            .map { it.getElementsByTag(HtmlConstants.ANCHOR_TAG) }
            .first()
            .attr(GogoanimePageStyles.DOWNLOAD_LINK_ATTRIBUTE)

    fun prepareDownloadLink(page: Document): String

    fun downloadEpisode(webDriver: RemoteWebDriver)

    fun findFileSize(url: String): Float
}
