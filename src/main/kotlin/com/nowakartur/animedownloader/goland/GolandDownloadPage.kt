package com.nowakartur.animedownloader.goland

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.util.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.stereotype.Service

@Service
class GolandDownloadPage {

    fun connectToGolandPage(webDriver: ChromeDriver, downloadUrl: String): ChromeDriver {
        webDriver.get(downloadUrl)
        return webDriver
    }

    fun findM4UploadDownloadLink(webDriver: ChromeDriver): String? {
        SeleniumUtil.waitFor(webDriver, By.className(GolandPageStyles.DOWNLOAD_CLASS))
        return webDriver.findElementsByClassName(GolandPageStyles.DOWNLOAD_CLASS)
            .find { it.text.contains(GolandPageStyles.M4_UPLOAD_TEXT) }
            ?.findElement(By.tagName(HtmlConstants.ANCHOR_TAG))
            ?.getAttribute(HtmlConstants.HREF_ATTRIBUTE)
    }
}
