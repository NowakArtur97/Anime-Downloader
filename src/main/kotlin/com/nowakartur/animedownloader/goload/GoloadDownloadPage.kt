package com.nowakartur.animedownloader.goload

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.goload.GoloadPageStyles.DOWNLOAD_CLASS
import com.nowakartur.animedownloader.goload.GoloadPageStyles.M4_UPLOAD_TEXT
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.stereotype.Service

@Service
class GoloadDownloadPage {

    fun connectToGolandPage(webDriver: ChromeDriver, downloadUrl: String): ChromeDriver {
        webDriver.get(downloadUrl)
        return webDriver
    }

    fun findM4UploadDownloadLink(webDriver: ChromeDriver): String? {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_CLASS))
        return webDriver.findElementsByClassName(DOWNLOAD_CLASS)
            .find { it.text.contains(M4_UPLOAD_TEXT) }
            ?.findElement(By.tagName(HtmlConstants.ANCHOR_TAG))
            ?.getAttribute(HtmlConstants.HREF_ATTRIBUTE)
    }
}
