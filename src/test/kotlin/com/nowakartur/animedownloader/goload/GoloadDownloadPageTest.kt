package com.nowakartur.animedownloader.goload

import com.nowakartur.animedownloader.download.goload.GoloadDownloadPage
import com.nowakartur.animedownloader.testUtil.SeleniumTest
import com.nowakartur.animedownloader.testUtil.TIME_TO_WAIT_FOR_ASSERTION
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.TimeUnit

class GoloadDownloadPageTest(@Autowired private val goloadDownloadPage: GoloadDownloadPage) : SeleniumTest() {

    @Test
    fun whenFindM4UploadLinkShouldReturnCorrectLink() {
        val goloadUrl =
            "https://goload.pro/download?id=MTg1MDMy&typesub=Gogoanime-SUB&title=Otome+Game+Sekai+wa+Mob+ni+Kibishii+Sekai+desu+Episode+4"
        val expectedLink = "http://www.mp4upload.com/6gy7hh1itnhe"
        goloadDownloadPage.connectToGolandPage(webDriver, goloadUrl)

        val actualLink = goloadDownloadPage.findM4UploadDownloadLink(webDriver)

        await.atMost(TIME_TO_WAIT_FOR_ASSERTION, TimeUnit.SECONDS).untilAsserted {
            assertEquals(expectedLink, actualLink)
        }
    }
}
