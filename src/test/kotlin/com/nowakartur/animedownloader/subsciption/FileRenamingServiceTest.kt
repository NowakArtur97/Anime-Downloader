package com.nowakartur.animedownloader.subsciption

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.io.File
import java.nio.file.Paths
import java.util.*

@SpringBootTest
@TestPropertySource(properties = ["app.scheduler.enabled=false", "app.csv.file-name=test-anime.csv"])
class FileRenamingServiceTest {

    @Test
    fun `when rename file should rename the newest file`() {
        val resourceDirectory = Paths.get("src", "test", "resources", "rename-file-test")
        val absolutePath = resourceDirectory.toFile().getAbsolutePath()
        val expectedFileName = "test-file-to-rename-${UUID.randomUUID()}"
        File("$absolutePath\\$expectedFileName").also {
            it.createNewFile()
        }

        FileRenamingService(absolutePath).renameNewestFileTo(expectedFileName)

        val actualFile = File("$absolutePath\\$expectedFileName.mp4")
        assertEquals("$expectedFileName.mp4", actualFile.name)

        actualFile.delete()
    }

    @Test
    fun `when rename to name with illegal characters should rename file, but remove illegal characters`() {
        val resourceDirectory = Paths.get("src", "test", "resources", "rename-file-test")
        val absolutePath = resourceDirectory.toFile().getAbsolutePath()
        val randomUUID = UUID.randomUUID()
        val expectedFileName = "test-file-to-rename-$randomUUID.mp4"
        val expectedFileNameWithIllegalCharacters = "test-file-to-rename\\/:\"?|<>*-$randomUUID"
        File("$absolutePath\\$expectedFileName").also {
            it.createNewFile()
        }

        FileRenamingService(absolutePath).renameNewestFileTo(expectedFileNameWithIllegalCharacters)

        val actualFile = File("$absolutePath\\$expectedFileName")
        assertEquals(expectedFileName, actualFile.name)

        actualFile.delete()
    }
}
