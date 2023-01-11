package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory

object GogoanimeEpisodePage {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun connectToEpisodePage(gogoanimeMainPageUrl: String, episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl")
        .get()

    fun findAllSupportedDownloadLinks(
        page: Document,
        supportedServers: List<DownloadPage>
    ): List<String> {
        val supportedDownloadServers =
            supportedServers.filter { page.getElementsByClass(it.episodePageDownloadLinkClass).isNotEmpty() }
        return supportedDownloadServers.map { it.prepareDownloadLink(page) }
    }

    fun mapToDownloadInfo(
        subscribedAnime: SubscribedAnimeEntity,
        allSupportedDownloadLinks: List<String>,
        supportedServers: List<DownloadPage>
    ): List<DownloadInfo> =
        allSupportedDownloadLinks.mapNotNull { link ->
            val downloadServer = supportedServers.find {
                it.episodePageDownloadLinkTexts.any { linkText -> link.contains(linkText) }
            }
            if (downloadServer != null) {
                val fileSize = downloadServer.findFileSize(link)
                if (fileSize >= subscribedAnime.minFileSize) {
                    DownloadInfo(subscribedAnime.title, downloadServer, fileSize, link)
                } else {
                    val serverName = downloadServer.toString().split(".")[4]
                    logger.info(
                        "The episode of: [${subscribedAnime.title}] is skipped for [$serverName], " +
                                "because the file is too small. " +
                                "File size: [$fileSize]. Required file size: [${subscribedAnime.minFileSize}]."
                    )
                    null
                }
            } else null
        }
}
