package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.testUtil.SeleniumTest
import org.junit.jupiter.api.Test

class StreamSbPageSeleniumTest : SeleniumTest() {

    @Test
    fun `when download file with three quality options from StreamSb should start downloading file`() {
        val episodeUrl = "https://ssbstream.net/d/9e6sw6cx06sb"
        processTest(StreamSbPage, episodeUrl)
    }

    @Test
    fun `when download file with two quality options from StreamSb should start downloading file`() {
        val episodeUrl = "https://sbplay2.xyz/d/n9oomq4pecsi"
        processTest(StreamSbPage, episodeUrl)
    }
}
