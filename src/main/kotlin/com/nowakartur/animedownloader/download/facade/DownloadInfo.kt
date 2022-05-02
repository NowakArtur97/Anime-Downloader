package com.nowakartur.animedownloader.download.facade

import com.nowakartur.animedownloader.download.common.DownloadPage

data class DownloadInfo(val downloadPage: DownloadPage, val fileSize: Float, val url: String)
