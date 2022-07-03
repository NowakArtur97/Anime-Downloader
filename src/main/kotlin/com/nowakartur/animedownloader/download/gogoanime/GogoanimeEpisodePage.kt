package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object GogoanimeEpisodePage {

    fun connectToEpisodePage(gogoanimeMainPageUrl: String, episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl")
        .get()

    fun findAllSupportedDownloadLinks(page: Document): List<String> {
        val supportedDownloadServers = listOf(Mp4UploadPage, StreamSbPage)
            .filter { page.getElementsByClass(it.episodePageDownloadLinkClass).isNotEmpty() }
        return supportedDownloadServers.map { it.prepareDownloadLink(page) }
    }

    fun mapToDownloadInfo(title: String, allSupportedDownloadLinks: List<String>): List<DownloadInfo> =
        allSupportedDownloadLinks.mapNotNull { link ->
            val downloadServer = listOf(Mp4UploadPage, StreamSbPage).find {
                link.contains(it.episodePageDownloadLinkText)
            }
            if (downloadServer != null) {
                DownloadInfo(title, downloadServer, downloadServer.findFileSize(link), link)
            } else null
        }
}
