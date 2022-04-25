package com.nowakartur.animedownloader.goland

import com.nowakartur.animedownloader.constant.HtmlConstants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

@Service
class GolandDownloadPage {

    fun connectToGolandPage(url: String): Document = Jsoup
        .connect(url)
        .get()

    fun findM4UploadDownloadLink(downloadPage: Document): String? =
        downloadPage.getElementsContainingText(GolandPageStyles.M4_UPLOAD_TEXT)
            .asSequence()
            .map { it.attr(HtmlConstants.HREF_ATTRIBUTE) }
            .firstOrNull()
}
