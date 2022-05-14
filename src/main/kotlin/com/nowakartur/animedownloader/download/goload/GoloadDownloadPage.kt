package com.nowakartur.animedownloader.download.goload

import com.nowakartur.animedownloader.constant.HtmlConstants
import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.DOWNLOADS_WRAPPER_CLASS
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.DOWNLOAD_CLASS
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.MP4_UPLOAD_TEXT
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.STREAM_SB_TEXT
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.X_STREAM_CDN_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnPage
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.openqa.selenium.By
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.remote.RemoteWebDriver

object GoloadDownloadPage {

    fun connectToGolandPage(webDriver: RemoteWebDriver, downloadUrl: String) {
        webDriver.get(downloadUrl)
    }

    fun findAllSupportedDownloadLinks(webDriver: RemoteWebDriver): List<String> {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_CLASS))
        val supportedDownloadLinksTexts = listOf(MP4_UPLOAD_TEXT, STREAM_SB_TEXT, X_STREAM_CDN_TEXT)
        return webDriver.findElementsByClassName(DOWNLOADS_WRAPPER_CLASS).last()
            .findElements(By.className(DOWNLOAD_CLASS)).map {
                it.findElement(By.tagName(HtmlConstants.ANCHOR_TAG))
                    .getAttribute(HtmlConstants.HREF_ATTRIBUTE)
            }
            .filter { supportedDownloadLinksTexts.any { text -> it.contains(text) } }
    }

    fun mapToDownloadInfo(title: String, allSupportedDownloadLinks: List<String>): List<DownloadInfo> =
        allSupportedDownloadLinks.map {
            if (it.contains(MP4_UPLOAD_TEXT)) {
                DownloadInfo(title, Mp4UploadPage, Mp4UploadPage.findFileSize(it), it)
            } else if (it.contains(STREAM_SB_TEXT)) {
                DownloadInfo(title, StreamSbPage, StreamSbPage.findFileSize(it), it)
            } else if (it.contains(X_STREAM_CDN_TEXT)) {
                DownloadInfo(title, XStreamCdnPage, XStreamCdnPage.findFileSize(it), it)
            } else {
                throw InvalidArgumentException("Download website is not supported for link: [$it].")
            }
        }
}
