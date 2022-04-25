package com.nowakartur.animedownloader.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants
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

    fun findAllSubscribedAnime(subscribedAnime: List<String>, page: Document): List<Element> = page
        .getElementsByClass(GogoanimePageStyles.MAIN_PAGE_ANIME_NAME_CLASS)
        .filter { node -> subscribedAnime.contains(node.text()) }

    fun findAllLinksToEpisodes(allSubscribedAnimeNodes: List<Element>, page: Document): List<String> =
        allSubscribedAnimeNodes
            .mapNotNull { it.children().first() }
            .map { it.attr(HtmlConstants.HREF_ATTRIBUTE) }
}
