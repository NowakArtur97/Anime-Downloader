package com.nowakartur.animedownloader.download.doodstream

import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.AFTER_SIZE_TEXT
import com.nowakartur.animedownloader.download.doodstream.DoodStreamStyles.FILE_SIZE_CLASS
import org.jsoup.Jsoup
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

    }

  
}
