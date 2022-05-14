package com.nowakartur.animedownloader.download.gogoanime

import com.nowakartur.animedownloader.selenium.ScreenshotUtil
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeService
import org.apache.commons.lang3.exception.ExceptionUtils
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class GogoanimeDownloadInfoThread(
    internal val subscribedAnimeService: SubscribedAnimeService,
    private val screenshotUtil: ScreenshotUtil,
) : Thread() {

    internal val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun cleanUpAfterException(
        e: Exception,
        subscribedAnimeEntity: SubscribedAnimeEntity,
        webDriver: RemoteWebDriver?
    ) {
        logger.error("Unexpected exception occurred when downloading episode of [${subscribedAnimeEntity.title}].")
        logger.info(e.message)
        val stackTrace: String = ExceptionUtils.getStackTrace(e)
        logger.error(stackTrace)

        subscribedAnimeService.waitForDownload(subscribedAnimeEntity)

        screenshotUtil.takeScreenshot(webDriver, subscribedAnimeEntity.title)
    }
}
