package com.nowakartur.animedownloader.gogoanime

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GogoanimeMainPage(@Value("\${app.gogoanime.url}") private val gogoanimeMainPageUrl: String) {

    fun connectToPage(): Document = Jsoup
        .connect(gogoanimeMainPageUrl)
        .get()

    fun findAllSubscribedAnimeLinks(subscribedAnime: List<String>, page: Document): List<Element> = page
        .getElementsByClass(GogoanimeMainPageStyles.ANIME_NAME_CLASS)
        .filter { node -> subscribedAnime.contains(node.text()) }
}
