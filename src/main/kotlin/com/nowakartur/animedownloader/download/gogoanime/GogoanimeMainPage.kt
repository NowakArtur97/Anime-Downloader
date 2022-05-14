package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.constant.HtmlConstants.HREF_ATTRIBUTE
import com.nowakartur.animedownloader.download.gogoanime.GogoanimePageStyles.MAIN_PAGE_ANIME_NAME_CLASS
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

//@Service
object GogoanimeMainPage {
//class GogoanimeMainPage(@Value("\${app.gogoanime.url}") private val gogoanimeMainPageUrl: String) {

    fun connectToMainPage(): Document = Jsoup
        .connect("https://gogoanime.sk")
        .get()

    fun findAllSubscribedAnime(subscribedAnime: List<SubscribedAnimeEntity>, page: Document): List<Element> {
        val titles = subscribedAnime.map { it.title }
        return page
            .getElementsByClass(MAIN_PAGE_ANIME_NAME_CLASS)
            .filter { node -> titles.any { node.text().contains(it, true) } }
    }

    fun findLinkToEpisodes(allSubscribedAnimeNodes: List<Element>, title: String): String =
        allSubscribedAnimeNodes
            .find { it.text().contains(title) }
            ?.children()
            ?.first()
            ?.attr(HREF_ATTRIBUTE)!!
}
