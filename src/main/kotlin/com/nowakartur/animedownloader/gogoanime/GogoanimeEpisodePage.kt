package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GogoanimeEpisodePage(@Value("\${app.gogoanime.url}") private val gogoanimeMainPageUrl: String) {

    fun connectToEpisodePage(episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl")
        .get()

    fun findLinkForDownload(episodePage: Document): String =
        episodePage.getElementsByClass(GogoanimePageStyles.EPISODE_PAGE_ANIME_DOWNLOAD_CLASS).asSequence()
            .mapNotNull { downloadButton ->
                downloadButton.children().asSequence().mapNotNull { link -> link.attr(HtmlConstants.HREF_ATTRIBUTE) }
                    .first()
            }.first()
}
