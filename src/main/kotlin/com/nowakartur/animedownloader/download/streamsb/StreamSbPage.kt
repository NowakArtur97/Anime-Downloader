package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.constant.HtmlConstants.SPAN_TAG
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.BEFORE_SIZE_TEXT
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.DOWNLOAD_LINK_CSS_SELECTOR
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.FILE_SIZES_CSS_SELECTOR
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object StreamSbPage : DownloadPage {

    override val episodePageDownloadLinkClass: String get() = "streamsb"
    override val episodePageDownloadLinkTexts: List<String>
        get() = listOf(
            episodePageDownloadLinkClass, "ssbstream", "streamsss", "sbone", "sbani"
        )

    override fun prepareDownloadLink(page: Document): String =
        getDownloadLink(page, episodePageDownloadLinkClass)
            .replace("/e/", "/d/")

    override fun findFileSize(url: String): Float {
        val page = Jsoup.connect(url).get()
        val qualityOptionDivs = page.select(FILE_SIZES_CSS_SELECTOR)
        val bestQualityOptionDiv = if (qualityOptionDivs.size > 2) {
            qualityOptionDivs[qualityOptionDivs.size - 2] // there are at least two options for the low and original quality
        } else {
            qualityOptionDivs.first() // there is only one link for the original quality
        }!!
        return bestQualityOptionDiv.getElementsByTag(SPAN_TAG).first()!!.text()
            .substringAfter(BEFORE_SIZE_TEXT)
            .replace(AFTER_SIZE_TEXT, "")
            .toFloat()
    }

    override fun downloadEpisode(webDriver: RemoteWebDriver) {

        chooseBestQuality(webDriver)

        clickFirstDownloadButton(webDriver)

        clickSecondDownloadButton(webDriver)
    }

    private fun chooseBestQuality(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.cssSelector(FILE_SIZES_CSS_SELECTOR))
        val qualityOptionDivs = webDriver.findElementsByCssSelector(FILE_SIZES_CSS_SELECTOR)
        val bestQualityOptionDiv = if (qualityOptionDivs.size > 2) {
            qualityOptionDivs[qualityOptionDivs.size - 2] // there are at least two options for the low and original quality
        } else {
            qualityOptionDivs.first() // there is only one link for the original quality
        }
        SeleniumUtil.clickUsingJavaScript(webDriver, bestQualityOptionDiv)
    }

    private fun clickFirstDownloadButton(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_BUTTON_CLASS))
        val downloadButton = webDriver.findElementByClassName(DOWNLOAD_BUTTON_CLASS)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadButton)
    }

    private fun clickSecondDownloadButton(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.cssSelector(DOWNLOAD_LINK_CSS_SELECTOR))
        val downloadLink = webDriver.findElementByCssSelector(DOWNLOAD_LINK_CSS_SELECTOR)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadLink)
    }
}
