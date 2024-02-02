package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.doodstream.DoodstreamPage
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

const val GOGOANIME_MAIN_PAGE_URL = "https://anitaku.to/home.html"

class GogoanimeEpisodePageTest {

    @Test
    fun `when find links for download should return correct links`() {
        val linkToAnimePage = "love-all-play-episode-7"
        val episodePage = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, linkToAnimePage)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/zdu7xouesiia.html",
            "https://sbone.pro/d/s2rrkvcv9nhl",
            "https://fembed9hd.com/f/ez40ni-62220den",
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)
        val actualLinks = GogoanimeEpisodePage.findAllSupportedDownloadLinks(
            episodePage,
            expectedSupportedServers
        )

        assertEquals(expectedLinks, actualLinks)
    }

    @Test
    fun `when get download info should return correct download info`() {
        val title = "Love All Play"
        val anime = SubscribedAnimeEntity(title)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/zdu7xouesiia.html",
            "https://ssbstream.net/d/s2rrkvcv9nhl",
            "https://fembed-hd.com/f/ez40ni-62220den",
        )
        val expectedDownloadInfo = listOf(
            DownloadInfo(title, Mp4UploadPage, 238.7f, "https://www.mp4upload.com/zdu7xouesiia.html"),
            DownloadInfo(title, StreamSbPage, 238.7f, "https://ssbstream.net/d/s2rrkvcv9nhl"),
            DownloadInfo(title, DoodstreamPage, 238.69f, "https://fembed-hd.com/f/ez40ni-62220den"),
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)

        val actualDownloadInfo = GogoanimeEpisodePage.mapToDownloadInfo(
            anime,
            expectedLinks,
            expectedSupportedServers
        )

        assertEquals(expectedDownloadInfo, actualDownloadInfo)
    }

    @Test
    fun `when get download info but file is too small should skip server`() {
        val title = "Love All Play"
        val anime = SubscribedAnimeEntity(title, minFileSize = 238.7f)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/zdu7xouesiia.html",
            "https://ssbstream.net/d/s2rrkvcv9nhl",
            "https://fembed-hd.com/f/ez40ni-62220den",
        )
        val expectedDownloadInfo = listOf(
            DownloadInfo(title, Mp4UploadPage, 238.7f, "https://www.mp4upload.com/zdu7xouesiia.html"),
            DownloadInfo(title, StreamSbPage, 238.7f, "https://ssbstream.net/d/s2rrkvcv9nhl"),
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)

        val actualDownloadInfo = GogoanimeEpisodePage.mapToDownloadInfo(
            anime,
            expectedLinks,
            expectedSupportedServers
        )

        assertEquals(expectedDownloadInfo, actualDownloadInfo)
    }

    @Test
    fun `when get download info but file is too small should skip title for all servers`() {
        val anime = SubscribedAnimeEntity("Love All Play", minFileSize = 900.0f)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/zdu7xouesiia.html",
            "https://ssbstream.net/d/s2rrkvcv9nhl",
            "https://fembed-hd.com/f/ez40ni-62220den",
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)

        val actualDownloadInfo = GogoanimeEpisodePage.mapToDownloadInfo(
            anime,
            expectedLinks,
            expectedSupportedServers
        )

        assertTrue(actualDownloadInfo.isEmpty())
    }
}
