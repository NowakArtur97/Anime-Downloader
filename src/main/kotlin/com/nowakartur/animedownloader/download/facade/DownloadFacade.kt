package com.nowakartur.animedownloader.download.facade

import com.nowakartur.animedownloader.download.doodstream.DoodStreamPage
import com.nowakartur.animedownloader.download.goload.GoloadDownloadPage
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.DOOD_STREAM_TEXT
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.MP4_UPLOAD_TEXT
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.STREAM_SB_TEXT
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.X_STREAM_CDN_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnPage
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory

object DownloadFacade {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadInBestQuality(webDriver: RemoteWebDriver, goloadLink: String) {

        GoloadDownloadPage.connectToGolandPage(webDriver, goloadLink)

        val allDownloadLinks = GoloadDownloadPage.findAllDownloadLinks(webDriver)

        val supportedDownloadLinksTexts = listOf(X_STREAM_CDN_TEXT)
//        val supportedDownloadLinksTexts = listOf(MP4_UPLOAD_TEXT, STREAM_SB_TEXT)
        val downloadInfo: List<DownloadInfo> = allDownloadLinks
            .filter { supportedDownloadLinksTexts.any { text -> it.contains(text) } }
            .map {
                if (it.contains(MP4_UPLOAD_TEXT)) {
                    DownloadInfo(Mp4UploadPage, Mp4UploadPage.findFileSize(it), it)
                } else if (it.contains(STREAM_SB_TEXT)) {
                    DownloadInfo(StreamSbPage, StreamSbPage.findFileSize(it), it)
                } else if (it.contains(DOOD_STREAM_TEXT)) {
                    DownloadInfo(DoodStreamPage, DoodStreamPage.findFileSize(it), it)
                } else if (it.contains(X_STREAM_CDN_TEXT)) {
                    DownloadInfo(XStreamCdnPage, XStreamCdnPage.findFileSize(it), it)
                } else {
                    throw InvalidArgumentException("Download website is not supported for link: [$it].")
                }
            }
        val bestQualityDownloadPage = downloadInfo.maxByOrNull { it.fileSize }!!
        logger.info("Link to the episode: [${bestQualityDownloadPage.url}].")
        bestQualityDownloadPage.downloadPage.connectToDownloadPage(webDriver, bestQualityDownloadPage.url)
        logger.info("Downloading the episode.")
        bestQualityDownloadPage.downloadPage.downloadEpisode(webDriver)
    }
}
