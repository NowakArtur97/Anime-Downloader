package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class StreamSbPageSeleniumTest : SeleniumTest() {

    @Test
    @Disabled
    fun `when download file from StreamSb should start downloading file`() {
        val episodeUrl = "https://awish.pro/f/bfwsast5z8lw_o"
        processTest(StreamSbPage, episodeUrl)
    }
}
