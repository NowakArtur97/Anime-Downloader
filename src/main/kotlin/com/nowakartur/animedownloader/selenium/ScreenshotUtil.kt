package com.nowakartur.animedownloader.selenium

import org.openqa.selenium.NoSuchSessionException
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.shooting.ShootingStrategies
import java.io.File
import java.io.IOException
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO

@Component
class ScreenshotUtil(
    @Value("\${app.screenshot.path}") private val screenshotPath: String,
    @Value("\${app.screenshot.file-extension}") private val fileExtension: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val simpleDateFormat: Format = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")

    fun takeScreenshot(webDriver: WebDriver?, screenshotName: String) {
        try {
            if (webDriver != null) {
                val screenshot: Screenshot = AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                    .takeScreenshot(webDriver)
                try {
                    val dateAsString = simpleDateFormat.format(Calendar.getInstance().time)
                    val filePathWithName =
                        "$PROJECT_PATH$screenshotPath$dateAsString-${
                            screenshotName
                                .replace(" ", "-")
                                .replace(Regex("[\"/:?*\"<>|]"), "")
                        }.$fileExtension"
                    ImageIO.write(screenshot.image, fileExtension, File(filePathWithName))
                } catch (e: IOException) {
                    logger.info("Can't take screenshot on path: [$PROJECT_PATH$screenshotPath].")
                    logger.info(e.message)
                }
            }
        } catch (e: NoSuchSessionException) {
            logger.info("Session ID is null.")
            logger.info(e.message)
        } catch (e: Exception) {
            logger.info("Exception when taking screenshot.")
            logger.info(e.message)
        }
    }

    companion object {
        private val PROJECT_PATH = System.getProperty("user.dir")
    }
}
