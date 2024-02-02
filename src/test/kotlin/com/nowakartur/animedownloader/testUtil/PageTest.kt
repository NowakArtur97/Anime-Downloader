package com.nowakartur.animedownloader.testUtil

import com.nowakartur.animedownloader.download.common.DownloadPage
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Assertions.assertEquals

open class PageTest {

    fun processFileSizeTest(downloadPage: DownloadPage, url: String, expectedFileSize: Float) {

        val actualFileSize = downloadPage.findFileSize(url)

        assertEquals(expectedFileSize, actualFileSize)
    }

    fun processDownloadLinkTest(downloadPage: DownloadPage, page: Document, expectedUrl: String) {

        val actualUrl = downloadPage.prepareDownloadLink(page)

        assertEquals(expectedUrl, actualUrl)
    }
}
