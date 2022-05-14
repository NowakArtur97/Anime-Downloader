package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants.HREF_ATTRIBUTE
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles.EPISODE_PAGE_ANIME_DOWNLOAD_CLASS
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object GogoanimeEpisodePage {

    fun connectToEpisodePage(gogoanimeMainPageUrl: String, episodeUrl: String): Document = Jsoup
        .connect("$gogoanimeMainPageUrl$episodeUrl")
        .get()

    fun findLinkForDownload(episodePage: Document): String =
        episodePage.getElementsByClass(EPISODE_PAGE_ANIME_DOWNLOAD_CLASS)
            .first()
            ?.children()
            ?.first()
            ?.attr(HREF_ATTRIBUTE)!!
}
