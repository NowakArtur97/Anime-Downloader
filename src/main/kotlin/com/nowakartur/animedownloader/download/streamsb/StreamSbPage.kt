package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.constant.HtmlConstants.ANCHOR_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.SPAN_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.TABLE_DATA_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.TABLE_ROW_TAG
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.CONTENT_CLASS
import com.nowakartur.animedownloader.download.streamsb.StreamSbStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StreamSbPage : DownloadPage {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun downloadEpisode(webDriver: ChromeDriver) {

        chooseBestQuality(webDriver)

        clickFirstDownloadButton(webDriver)

        clickSecondDownloadButton(webDriver)
    }

    private fun chooseBestQuality(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.tagName(TABLE_ROW_TAG))
        val tableRows = webDriver.findElementsByTagName(TABLE_ROW_TAG)
        val bestQualityLinkRow = if (tableRows.size > 2) {
            tableRows[tableRows.size - 2] // there are at least two options for the low and original quality
        } else {
            tableRows.last() // there is only one link for the original quality
        }
        val bestQualityLinkTableData = bestQualityLinkRow
            .findElements(By.tagName(TABLE_DATA_TAG))
            .first()
        val bestQualityLink = bestQualityLinkTableData.findElement(By.tagName(ANCHOR_TAG))
        SeleniumUtil.clickUsingJavaScript(webDriver, bestQualityLink)
    }

    private fun clickFirstDownloadButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_BUTTON_CLASS))
        val downloadButton = webDriver.findElementByClassName(DOWNLOAD_BUTTON_CLASS)
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadButton)
    }

    private fun clickSecondDownloadButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(CONTENT_CLASS))
        val spanElementWithLink = webDriver.findElementsByTagName(SPAN_TAG).first()
        SeleniumUtil.waitFor(webDriver, spanElementWithLink)
        val downloadLink = spanElementWithLink.findElement(By.tagName(ANCHOR_TAG))
        SeleniumUtil.clickUsingJavaScript(webDriver, downloadLink)
        logger.info(downloadLink.text)
    }
}
