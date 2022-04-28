package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants.HREF_ATTRIBUTE
import com.nowakartur.animedownloader.gogoanime.GogoanimePageStyles.EPISODE_PAGE_ANIME_DOWNLOAD_CLASS
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
        episodePage.getElementsByClass(EPISODE_PAGE_ANIME_DOWNLOAD_CLASS)
            .first()
            ?.children()
            ?.first()
            ?.attr(HREF_ATTRIBUTE)!!
}
