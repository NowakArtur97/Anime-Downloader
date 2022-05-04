package com.nowakartur.animedownloader.download.goload

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.DOWNLOAD_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object GoloadDownloadPage {

    fun connectToGolandPage(webDriver: RemoteWebDriver, downloadUrl: String) {
        webDriver.get(downloadUrl)
    }

    fun findAllDownloadLinks(webDriver: RemoteWebDriver): List<String> {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_CLASS))
        return webDriver.findElementsByClassName(GoloadPageStyles.DOWNLOADS_WRAPPER_CLASS)
            .last()
            .findElements(By.className(DOWNLOAD_CLASS))
            .map {
                it.findElement(By.tagName(HtmlConstants.ANCHOR_TAG))
                    .getAttribute(HtmlConstants.HREF_ATTRIBUTE)
            }
    }
}
