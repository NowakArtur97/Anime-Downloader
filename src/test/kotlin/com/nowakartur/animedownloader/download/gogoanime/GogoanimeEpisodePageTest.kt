package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.download.common.DownloadInfo
import com.nowakartur.animedownloader.download.doodstream.DoodstreamPage
import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

const val GOGOANIME_MAIN_PAGE_URL = "https://anitaku.to/home.html"

class GogoanimeEpisodePageTest {

    @Test
    @Disabled
    fun `when find links for download should return correct links`() {
        val linkToAnimePage = "/love-all-play-episode-8"
        val episodePage = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, linkToAnimePage)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/embed-82fuff8awpfa.html",
            "https://awish.pro/f/j02oxoaiwxsy_x",
            "https://dood.wf/e/t10qgg29wpij",
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)
        val actualLinks = GogoanimeEpisodePage.findAllSupportedDownloadLinks(
            episodePage,
            expectedSupportedServers
        )

        assertEquals(expectedLinks, actualLinks)
    }

    @Test
    @Disabled
    fun `when find episode number should return correct number`() {
        val linkToAnimePage = "/love-all-play-episode-8"
        val episodePage = GogoanimeEpisodePage.connectToEpisodePage(GOGOANIME_MAIN_PAGE_URL, linkToAnimePage)
        val expectedEpisodeNumber = "8"

        val actualEpisodeNumber = GogoanimeEpisodePage.findEpisodeNumber(episodePage)

        assertEquals(expectedEpisodeNumber, actualEpisodeNumber)
    }

    @Test
    @Disabled
    fun `when get download info should return correct download info`() {
        val title = "Sokushi Cheat ga Saikyou sugite, Isekai no Yatsura ga Marude Aite ni Naranai n desu ga."
        val anime = SubscribedAnimeEntity(title)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/ig5hgyfg9wqw.html",
            "https://awish.pro/f/x32o6bkjndap_x",
            "https://dood.wf/e/4c3ihky9458j",
        )
        val expectedDownloadInfo = listOf(
            DownloadInfo(title, Mp4UploadPage, 438.1f, "https://www.mp4upload.com/ig5hgyfg9wqw.html", "5"),
            DownloadInfo(title, StreamSbPage, 438.1f, "https://awish.pro/f/x32o6bkjndap_x", "5"),
            DownloadInfo(title, DoodstreamPage, 433.1f, "https://dood.wf/e/4c3ihky9458j", "5"),
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)

        val actualDownloadInfo = GogoanimeEpisodePage.mapToDownloadInfo(
            anime,
            expectedLinks,
            expectedSupportedServers,
            "5"
        )

        assertEquals(expectedDownloadInfo, actualDownloadInfo)
    }

    @Test
    @Disabled
    fun `when get download info but file is too small should skip server`() {
        val title = "Sokushi Cheat ga Saikyou sugite, Isekai no Yatsura ga Marude Aite ni Naranai n desu ga."
        val anime = SubscribedAnimeEntity(title, minFileSize = 280.0f)
        val expectedLinks = listOf(
            "https://www.mp4upload.com/qpw4tz4v7jy7.html",
            "https://awish.pro/f/4phhx9y31q0u_x",
            "https://dood.wf/d/ip0jfc8q092h",
        )
        val expectedDownloadInfo = listOf(
            DownloadInfo(title, Mp4UploadPage, 281.2f, "https://www.mp4upload.com/qpw4tz4v7jy7.html", "4"),
        )
        val expectedSupportedServers = listOf(Mp4UploadPage, StreamSbPage, DoodstreamPage)

        val actualDownloadInfo = GogoanimeEpisodePage.mapToDownloadInfo(
            anime,
            expectedLinks,
            expectedSupportedServers,
            "4"
        )

        assertEquals(expectedDownloadInfo, actualDownloadInfo)
    }

    @Test
    @Disabled
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
            expectedSupportedServers,
            "7"
        )

        assertTrue(actualDownloadInfo.isEmpty())
    }
}
