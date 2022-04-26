package com.nowakartur.animedownloader.goland

import com.nowakartur.animedownloader.constant.HtmlConstants
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.stereotype.Service

@Service
class GolandDownloadPage {

    fun connectToGolandPage(downloadUrl: String): ChromeDriver {
        WebDriverManager.chromedriver().setup()
        val webDriver = ChromeDriver()
        webDriver.get(downloadUrl)
        return webDriver
    }

    fun findM4UploadDownloadLink(webDriver: ChromeDriver): String? {
        val wait = WebDriverWait(webDriver, 15)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(GolandPageStyles.DOWNLOAD_CLASS)))
        return webDriver.findElementsByClassName(GolandPageStyles.DOWNLOAD_CLASS)
            .find { it.text.contains(GolandPageStyles.M4_UPLOAD_TEXT) }
            ?.findElement(By.tagName(HtmlConstants.ANCHOR_TAG))
            ?.getAttribute(HtmlConstants.HREF_ATTRIBUTE)
    }
}
