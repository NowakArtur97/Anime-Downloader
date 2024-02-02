package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.FILE_SIZE_CLASS
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.SECOND_DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object StreamSbPage : DownloadPage {

    override val episodePageDownloadLinkClass: String get() = "streamwish"
    override val episodePageDownloadLinkTexts: List<String> get() = listOf("awish")

    override fun prepareDownloadLink(page: Document): String =
        getDownloadLink(page, episodePageDownloadLinkClass).replace("/e/", "/f/") + "_x"

    override fun findFileSize(url: String): Float {
        val page = Jsoup.connect(url).get()
        val fileSizeElement = page.getElementsByClass(FILE_SIZE_CLASS)
        return fileSizeElement.first()!!.text().replace(" ", "").replace(AFTER_SIZE_TEXT, "").toFloat()
    }

    override fun downloadEpisode(webDriver: RemoteWebDriver) {

        clickFirstDownloadButton(webDriver)
        clickSecondDownloadButton(webDriver)
    }

    private fun clickFirstDownloadButton(webDriver: RemoteWebDriver) =
        clickDownloadButton(webDriver, DOWNLOAD_BUTTON_CLASS)

    private fun clickSecondDownloadButton(webDriver: RemoteWebDriver) =
        clickDownloadButton(webDriver, SECOND_DOWNLOAD_BUTTON_CLASS)

    private fun clickDownloadButton(webDriver: RemoteWebDriver, buttonClass: String) {
        SeleniumUtil.waitFor(webDriver, By.className(buttonClass))
        val downloadButton = webDriver.findElementByClassName(buttonClass)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadButton)
    }
}
