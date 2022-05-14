//package com.nowakartur.animedownloader.download.facade
//
//import com.nowakartur.animedownloader.download.goload.GoloadPageStyles.MP4_UPLOAD_TEXT
//import com.nowakartur.animedownloader.download.mp4upload.Mp4UploadPage
//import com.nowakartur.animedownloader.download.streamsb.StreamSbPage
//import com.nowakartur.animedownloader.download.xstreamcdn.XStreamCdnPage
//import com.nowakartur.animedownloader.selenium.SeleniumUtil
//import com.nowakartur.animedownloader.testUtil.SeleniumTest
//import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
//import org.awaitility.kotlin.await
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//import java.util.concurrent.TimeUnit
//
//class DownloadFacadeTest : SeleniumTest() {
//
//    @Test
//    fun `when get download info should return correct download info`() {
//        val goloadUrl = "https://goload.pro/download?id=MTg2MjMw&typesub=Gogoanime-SUB&title=Tomodachi+Game+Episode+6"
//        val expectedDownloadInfo = listOf(
//            DownloadInfo(XStreamCdnPage, 314.35f, "https://fembed-hd.com/f/-867japek5e34zj"),
//            DownloadInfo(Mp4UploadPage, 314.3f, "http://www.mp4upload.com/tfhmno4rrqwa"),
//            DownloadInfo(StreamSbPage, 220.2f, "https://sbplay2.xyz/d/wok4s5ql6ie6"),
//        )
//
//        val actualDownloadInfo = DownloadFacade.getDownloadInfo(webDriver, goloadUrl)
//
//        assertEquals(expectedDownloadInfo, actualDownloadInfo)
//    }
//
//    @Test
//    fun `when download file in best quality should start downloading file from Mp4Upload`() {
//        val mp4uploadUrl = "https://www.mp4upload.com/4fcl95whsq23"
//
//        DownloadFacade.downloadInBestQuality(webDriver, DownloadInfo(Mp4UploadPage, 259.0f, mp4uploadUrl))
//
//        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
//            assertTrue(webDriver.currentUrl.contains(MP4_UPLOAD_TEXT))
//        }
//        SeleniumUtil.switchToDownloadTab(webDriver)
//        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
//            assertTrue(SeleniumUtil.isDownloading(webDriver))
//        }
//    }
//}
