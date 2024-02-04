package com.nowakartur.animedownloader.subsciption

import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.absolutePathString

@Service
class FileRenamingService(
    @Value("\${app.download-directory}") private val downloadDirectory: String,
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun renameNewestEpisodeTo(title: String, episodeNumber: String) {
        val actualDownloadDirectory = if (StringUtils.isNotBlank(downloadDirectory)) downloadDirectory
        else System.getProperty("user.dir") + "\\Downloads"

        val directory: Path = Paths.get(actualDownloadDirectory)

        val newestFile: Optional<Path> = findNewestFile(directory)

        if (newestFile.isPresent) {
            val titleWithoutIllegalCharacters = removeIllegalCharacters(title)
            val newFullFileName = "${directory.absolutePathString()}\\$titleWithoutIllegalCharacters $episodeNumber.mp4"
            val renamedFile = File(newFullFileName)
            val renamedFilePath = newestFile.get().absolutePathString()
            val fileToRename = File(renamedFilePath)
            val wasRenamed = fileToRename.renameTo(renamedFile)
            if (wasRenamed) {
                logger.info("File was successfully renamed to: [$newFullFileName]")
            }
        }
    }

    private fun findNewestFile(dir: Path) =
        Files.list(dir)
            .filter { f -> !Files.isDirectory(f) }
            .max(Comparator.comparingLong { f -> f.toFile().lastModified() })

    private fun removeIllegalCharacters(newFileName: String): String = newFileName
        .replace("\\", "")
        .replace("/", "")
        .replace(":", "")
        .replace("\"", "")
        .replace("?", "")
        .replace("*", "")
        .replace("|", "")
        .replace("<", "")
        .replace(">", "")
}
