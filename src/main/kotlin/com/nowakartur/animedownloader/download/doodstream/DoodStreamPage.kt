package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.constant.HtmlConstants.ANCHOR_TAG
import com.nowakartur.animedownloader.constant.HtmlConstants.SPAN_TAG
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.DOWNLOAD_CONTENT_CLASS
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.FILE_SIZE_CLASS
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.REDIRECT_CONTAINER_CLASS
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.REDIRECT_CONTENT_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver

object DoodStreamPage : DownloadPage {

    override fun findFileSize(url: String): Float = Jsoup
        .connect(url)
        .userAgent("Chrome")
        .get()
        .getElementsByClass(FILE_SIZE_CLASS)
        .first()!!
        .text()
        .substringBefore(AFTER_SIZE_TEXT)
        .toFloat()

    override fun downloadEpisode(webDriver: ChromeDriver) {
        clickRedirectButton(webDriver)
        clickDownloadButton(webDriver)
    }

    private fun clickRedirectButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(REDIRECT_CONTAINER_CLASS))
        val firstRedirectButton = webDriver.findElementByClassName(REDIRECT_CONTAINER_CLASS)
            .findElement(By.tagName(ANCHOR_TAG))
        SeleniumUtil.clickUsingJavaScript(webDriver, firstRedirectButton)
        SeleniumUtil.waitFor(webDriver, By.className(REDIRECT_CONTENT_CLASS))
        val secondRedirectButton = webDriver.findElementByClassName(REDIRECT_CONTENT_CLASS)
            .findElement(By.tagName(ANCHOR_TAG))
            .findElement(By.tagName(SPAN_TAG))
        SeleniumUtil.clickUsingJavaScript(webDriver, secondRedirectButton)
    }

    private fun clickDownloadButton(webDriver: ChromeDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_CONTENT_CLASS))
        webDriver.findElementByClassName(DOWNLOAD_CONTENT_CLASS)
            .findElement(By.tagName(ANCHOR_TAG))
            .click()
    }
}