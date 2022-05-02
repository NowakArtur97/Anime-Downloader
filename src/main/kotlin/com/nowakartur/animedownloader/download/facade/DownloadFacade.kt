package com.nowakartur.animedownloader.download

import com.nowakartur.animedownloader.download.facade.DownloadInfo
import com.nowakartur.animedownloader.download.goload.GoloadPageStyles
import com.nowakartur.animedownloader.download.m4upload.M4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.stereotype.Service

@Service
class DownloadFacade {
    fun downloadInBestQuality(webDriver: ChromeDriver, allDownloadLinks: List<String>) {
        val downloadInfo: List<DownloadInfo> = allDownloadLinks.map {
            if (it.contains(GoloadPageStyles.M4_UPLOAD_TEXT)) {
                DownloadInfo(M4UploadPage, M4UploadPage.findFileSize(it), it)
            } else if (it.contains(GoloadPageStyles.STREAM_SB_UPLOAD_TEXT)) {
                DownloadInfo(StreamSbPage, StreamSbPage.findFileSize(it), it)
            } else {
                DownloadInfo(M4UploadPage, 0f, it)
            }
        }
        val bestQualityDownloadPage = downloadInfo.maxByOrNull { it.fileSize }
        bestQualityDownloadPage!!.downloadPage.connectToDownloadPage(webDriver, bestQualityDownloadPage.url)
    }
}
