package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants.H1_TAG
import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.common.DownloadPage
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles.AFTER_EPISODE_NUMBER_TEXT
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles.BEFORE_EPISODE_NUMBER_TEXT
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles.EPISODE_NUMBER_TEXT_CLASS
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

object GogoanimeEpisodePage {

    private const val DEFAULT_FILE_SIZE = 0.0f
    private val logger = LoggerFactory.getLogger(javaClass)

    fun connectToEpisodePage(gogoanimeMainPageUrl: String, episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl".replace("/home.html", ""))
        .get()

    fun findAllSupportedDownloadLinks(
        page: Document,
        supportedServers: List<DownloadPage>
    ): List<String> {
        val supportedDownloadServers =
            supportedServers.filter { page.getElementsByClass(it.episodePageDownloadLinkClass).isNotEmpty() }
        return supportedDownloadServers.map { it.prepareDownloadLink(page) }
    }

    fun findEpisodeNumber(
        page: Document,
    ): String = page.getElementsByClass(EPISODE_NUMBER_TEXT_CLASS)
        .first()!!
        .getElementsByTag(H1_TAG)
        .text()
        .substringAfter(BEFORE_EPISODE_NUMBER_TEXT)
        .substringBefore(AFTER_EPISODE_NUMBER_TEXT)

    fun mapToDownloadInfo(
        subscribedAnime: SubscribedAnimeEntity,
        allSupportedDownloadLinks: List<String>,
        supportedServers: List<DownloadPage>,
        episodeNumber: String
    ): List<DownloadInfo> =
        allSupportedDownloadLinks.mapNotNull { link ->
            val downloadServer = supportedServers.find {
                it.episodePageDownloadLinkTexts.any { linkText -> link.contains(linkText) }
            }
            if (downloadServer != null) {
                val fileSize = getFileSize(downloadServer, link)
                if (fileSize >= subscribedAnime.minFileSize) {
                    DownloadInfo(subscribedAnime.title, downloadServer, fileSize, link, episodeNumber)
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

    private fun getFileSize(
        downloadServer: DownloadPage,
        link: String
    ) = try {
        downloadServer.findFileSize(link)
    } catch (e: Exception) {
        when (e) {
            is SocketTimeoutException, is HttpStatusException, is NullPointerException -> DEFAULT_FILE_SIZE
            else -> throw e
        }
    }
}
