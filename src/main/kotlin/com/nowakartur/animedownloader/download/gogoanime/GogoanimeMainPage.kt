package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants.HREF_ATTRIBUTE
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles.MAIN_PAGE_ANIME_NAME_CLASS
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object GogoanimeMainPage {

    fun connectToMainPage(gogoanimeMainPageUrl: String): Document = Jsoup
        .connect(gogoanimeMainPageUrl)
        .get()

    fun findAllSubscribedAnime(subscribedAnime: List<SubscribedAnimeEntity>, page: Document): List<Element> {
        val titles = subscribedAnime.map { it.title }
        return page
            .getElementsByClass(MAIN_PAGE_ANIME_NAME_CLASS)
            .filter { node -> titles.any { node.text().contains(it, true) } }
    }

    fun findLinkToEpisodeByTitle(allSubscribedAnimeNodes: List<Element>, title: String): String =
        allSubscribedAnimeNodes
            .find { it.text().contains(title) }
            ?.children()
            ?.first()
            ?.attr(HREF_ATTRIBUTE)!!
}
