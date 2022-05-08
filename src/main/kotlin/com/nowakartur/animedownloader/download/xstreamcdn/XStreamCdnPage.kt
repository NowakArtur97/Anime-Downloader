package com.nowakartur.animedownloader.download.xstreamcdn

import com.nowakartur.animedownloader.constant.HtmlConstants.H2_TAG
import com.nowakartur.animedownloader.download.common.DownloadPage
import org.jsoup.Jsoup
import org.openqa.selenium.remote.RemoteWebDriver

object XStreamCdnPage : DownloadPage {

    override fun findFileSize(url: String): Float = Jsoup
        .connect(url)
        .get()
        .getElementsByTag(H2_TAG)
        .first()!!
        .text()
        .substringAfter(XStreamCdnStyles.BEFORE_SIZE_TEXT)
        .substringBefore(XStreamCdnStyles.AFTER_SIZE_TEXT)
        .toFloat()

    override fun downloadEpisode(webDriver: RemoteWebDriver) {

    }
}
