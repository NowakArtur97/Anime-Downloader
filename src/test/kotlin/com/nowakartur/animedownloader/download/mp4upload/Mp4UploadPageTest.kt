package com.nowakartur.animedownloader.download.mp4upload

import com.nowakartur.animedownloader.download.gogoanime.GOGOANIME_MAIN_PAGE_URL
import com.nowakartur.animedownloader.download.gogoanime.GogoanimeEpisodePage
import com.nowakartur.animedownloader.testUtil.PageTest
import org.junit.jupiter.api.Test

class Mp4UploadPageTest : PageTest() {

    @Test
    fun `when check file size from Mp4Upload should return correct value`() {
        val mp4UploadUrl = "https://www.mp4upload.com/syoofo92jare.html"
        val expectedFileSize = 347.6f

        processFileSizeTest(Mp4UploadPage, mp4UploadUrl, expectedFileSize)
    }

    @Test
    fun `when prepare download link should return correct value`() {
        val expectedMp4UploadUrl = "https://www.mp4upload.com/syoofo92jare.html"

        val page = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, "/yurei-deco-episode-1")

        processDownloadLinkTest(Mp4UploadPage, page, expectedMp4UploadUrl)
    }
}
