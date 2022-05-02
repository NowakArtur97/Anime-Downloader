package com.nowakartur.animedownloader.download.streamsb

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StreamSbPageTest {

    @Test
    fun `when check file size with three quality options from StreamSb should return correct value`() {
        val streamSbUrl = "https://sbplay2.xyz/d/j094za1pv1wg"
        val expectedFileSize = 250.9f

        val actualFileSize = StreamSbPage.findFileSize(streamSbUrl)

        assertEquals(expectedFileSize, actualFileSize)
    }

    @Test
    fun `when check file size with two quality options from StreamSb should return correct value`() {
        val streamSbUrl = "https://sbplay2.xyz/d/n9oomq4pecsi"
        val expectedFileSize = 113.5f

        val actualFileSize = StreamSbPage.findFileSize(streamSbUrl)

        assertEquals(expectedFileSize, actualFileSize)
    }
}
