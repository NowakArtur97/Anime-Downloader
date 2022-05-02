package com.nowakartur.animedownloader.download.facade

import com.nowakartur.animedownloader.download.goload.GoloadDownloadPage
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.MP4_UPLOAD_TEXT
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.STREAM_SB_UPLOAD_TEXT
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.chrome.ChromeDriver
import org.slf4j.LoggerFactory

object DownloadFacade {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadInBestQuality(webDriver: ChromeDriver, goloadLink: String) {

        GoloadDownloadPage.connectToGolandPage(webDriver, goloadLink)

        val allDownloadLinks = GoloadDownloadPage.findAllDownloadLinks(webDriver)

        val supportedDownloadLinksTexts = listOf(MP4_UPLOAD_TEXT, STREAM_SB_UPLOAD_TEXT)
        val downloadInfo: List<DownloadInfo> = allDownloadLinks
            .filter { supportedDownloadLinksTexts.any { text -> it.contains(text) } }
            .map {
                if (it.contains(MP4_UPLOAD_TEXT)) {
                    DownloadInfo(Mp4UploadPage, Mp4UploadPage.findFileSize(it), it)
                } else if (it.contains(STREAM_SB_UPLOAD_TEXT)) {
                    DownloadInfo(StreamSbPage, StreamSbPage.findFileSize(it), it)
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
