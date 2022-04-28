package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants.HREF_ATTRIBUTE
import com.nowakartur.animedownloader.gogoanime.GogoanimePageStyles.MAIN_PAGE_ANIME_NAME_CLASS
import com.nowakartur.animedownloader.subsciption.SubscribedAnimeEntity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GogoanimeMainPage(@Value("\${app.gogoanime.url}") private val gogoanimeMainPageUrl: String) {

    fun connectToMainPage(): Document = Jsoup
        .connect(gogoanimeMainPageUrl)
        .get()

    fun findAllSubscribedAnime(subscribedAnime: List<SubscribedAnimeEntity>, page: Document): List<Element> {
        val titles = subscribedAnime.map { it.title }
        return page
            .getElementsByClass(MAIN_PAGE_ANIME_NAME_CLASS)
            .filter { node -> titles.any { node.text().contains(it) } }
    }

    fun findLinkToEpisodes(allSubscribedAnimeNodes: List<Element>, title: String): String =
        allSubscribedAnimeNodes
            .find { it.text().contains(title) }
            ?.children()
            ?.first()
            ?.attr(HREF_ATTRIBUTE)!!
}
