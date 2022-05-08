package com.nowakartur.animedownloader.testUtil

import com.nowakartur.animedownloader.download.common.DownloadPage
import org.junit.jupiter.api.Assertions.assertEquals

open class PageTest {

    fun processTest(downloadPage: DownloadPage, url: String, expectedFileSize: Float) {

        val actualFileSize = downloadPage.findFileSize(url)

        assertEquals(expectedFileSize, actualFileSize)
    }
}
