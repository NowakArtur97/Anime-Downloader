package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

const val GOGOANIME_MAIN_PAGE_URL = "https://gogoanime.lu/"

class GogoanimeEpisodePageTest {

    @Test
    fun `when find links for download should return correct links`() {
        val linkToAnimePage = "love-all-play-episode-7"
        val episodePage = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, linkToAnimePage)
        val expectedLinks = listOf(
//            "https://fembed-hd.com/f/ez40ni-62220den", // TODO
            "https://www.mp4upload.com/zdu7xouesiia.html",
            "https://ssbstream.net/d/s2rrkvcv9nhl",
        )
        val actualLinks = GogoanimeEpisodePage.findAllSupportedDownloadLinks(episodePage)

        assertEquals(expectedLinks, actualLinks)
    }

    @Test
    fun `when get download info should return correct download info`() {
        val title = "Love All Play"
        val expectedLinks = listOf(
//           "https://fembed-hd.com/f/ez40ni-62220den", // TODO
            "https://www.mp4upload.com/zdu7xouesiia.html",
            "https://ssbstream.net/d/s2rrkvcv9nhl",
        )
        val expectedDownloadInfo = listOf(
//            DownloadInfo(title, XStreamCdnPage, 314.35f, "https://fembed-hd.com/f/ez40ni-62220den"),
            DownloadInfo(title, Mp4UploadPage, 238.7f, "https://www.mp4upload.com/zdu7xouesiia.html"),
            DownloadInfo(title, StreamSbPage, 238.7f, "https://ssbstream.net/d/s2rrkvcv9nhl"),
        )
        val actualDownloadInfo = GogoanimeEpisodePage.mapToDownloadInfo(title, expectedLinks)

        assertEquals(expectedDownloadInfo, actualDownloadInfo)
    }
}
