package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnPage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.InvalidArgumentException

object GogoanimeEpisodePage {

    fun connectToEpisodePage(gogoanimeMainPageUrl: String, episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl")
        .get()

    fun findAllSupportedDownloadLinks(page: Document): List<String> {

        val supportedDownloadServers = listOf(Mp4UploadPage, StreamSbPage, XStreamCdnPage)
        return supportedDownloadServers.map { it.prepareDownloadLink(page) }
    }

    fun mapToDownloadInfo(title: String, allSupportedDownloadLinks: List<String>): List<DownloadInfo> =
        allSupportedDownloadLinks.map { link ->
            val downloadServer = listOf(Mp4UploadPage, StreamSbPage, XStreamCdnPage).find {
                link.contains(it.downloadLinkText)
            } ?: throw InvalidArgumentException("Download website is not supported for link: [linkit].")
            DownloadInfo(title, downloadServer, downloadServer.findFileSize(link), link)
        }
}
