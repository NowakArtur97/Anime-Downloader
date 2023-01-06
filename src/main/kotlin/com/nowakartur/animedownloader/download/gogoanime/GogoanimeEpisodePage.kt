package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnPage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object GogoanimeEpisodePage {

    private val supportedServers = listOf(Mp4UploadPage, StreamSbPage, XStreamCdnPage)

    fun connectToEpisodePage(gogoanimeMainPageUrl: String, episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl")
        .get()

    fun findAllSupportedDownloadLinks(page: Document): List<String> {
        val supportedDownloadServers =
            supportedServers.filter { page.getElementsByClass(it.episodePageDownloadLinkClass).isNotEmpty() }
        return supportedDownloadServers.map { it.prepareDownloadLink(page) }
    }

    fun mapToDownloadInfo(title: String, allSupportedDownloadLinks: List<String>): List<DownloadInfo> =
        allSupportedDownloadLinks.mapNotNull { link ->
            val downloadServer = supportedServers.find {
                it.episodePageDownloadLinkTexts.any { linkText -> link.contains(linkText) }
            }
            if (downloadServer != null) {
                DownloadInfo(title, downloadServer, downloadServer.findFileSize(link), link)
            } else null
        }
}
