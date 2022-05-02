package com.nowakartur.animedownloader.download.goload

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.DOWNLOAD_CLASS
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver

object GoloadDownloadPage {

    fun connectToGolandPage(webDriver: ChromeDriver, downloadUrl: String): ChromeDriver {
        webDriver.get(downloadUrl)
        return webDriver
    }

    fun findAllDownloadLinks(webDriver: ChromeDriver): List<String> {
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
