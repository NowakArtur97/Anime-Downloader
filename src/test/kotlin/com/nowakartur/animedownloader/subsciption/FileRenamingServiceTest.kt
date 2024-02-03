package com.nowakartur.animedownloader.subsciption

import org.junit.jupiter.api.Assertions.assertTrue
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
        val expectedFileName = "test-file-to-rename-${UUID.randomUUID()}.mp4"
        File("$absolutePath\\$expectedFileName").also {
            it.createNewFile()
        }

        FileRenamingService(absolutePath).renameNewestFileTo(expectedFileName)

        val actualFile = File("$absolutePath\\$expectedFileName.mp4")
        assertTrue(actualFile.isFile)

        actualFile.delete()
    }
}
