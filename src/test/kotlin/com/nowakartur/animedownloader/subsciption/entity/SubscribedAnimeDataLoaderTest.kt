package com.nowakartur.animedownloader.subsciption.entity

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = ["app.scheduler.enabled=false", "app.csv.file-name=test-anime.csv"])
class SubscribedAnimeDataLoaderTest(
    @Autowired private val subscribedAnimeDataLoader: SubscribedAnimeDataLoader,
    @Autowired private val subscribedAnimeRepository: SubscribedAnimeRepository,
) {

    @AfterEach
    fun initializeWebDriver() {
        subscribedAnimeRepository.deleteAll()
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when there is IN_PROGRESS title should update status to TO_DOWNLOAD`() {
        val exampleTitle = subscribedAnimeRepository.findAll().first()
        exampleTitle.changeStatusToInProgress()
        subscribedAnimeRepository.save(exampleTitle)

        subscribedAnimeDataLoader.prepareDataOnStartup()

        assertTrue(subscribedAnimeRepository.findAll().all { it.status == SubscribedAnimeStatus.TO_DOWNLOAD })
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when there is changed priority compared to csv file should update priority`() {
        val exampleTitle = subscribedAnimeRepository.findAll().first()
        val originalPriority = exampleTitle.priority
        exampleTitle.priority = SubscribedAnimePriority.HIGH
        subscribedAnimeRepository.save(exampleTitle)

        subscribedAnimeDataLoader.prepareDataOnStartup()

        assertEquals(originalPriority, subscribedAnimeRepository.findAll().first().priority)
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when there is no change in priorities compared to csv file should not update any title`() {
        val originalTitles = subscribedAnimeRepository.findAll()

        subscribedAnimeDataLoader.prepareDataOnStartup()

        assertEquals(originalTitles, subscribedAnimeRepository.findAll())
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when app starts should save new titles from csv file`() {
        val numberOfTitles = subscribedAnimeRepository.count()

        assertEquals(3, numberOfTitles)
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when app starts without new titles in csv file should not save any more titles`() {
        var numberOfTitles = subscribedAnimeRepository.count()
        assertEquals(3, numberOfTitles)

        subscribedAnimeDataLoader.prepareDataOnStartup()

        numberOfTitles = subscribedAnimeRepository.count()
        assertEquals(3, numberOfTitles)
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when there is an old title should be removed`() {
        val exampleTitle = SubscribedAnimeEntity("new title not in csv")
        subscribedAnimeRepository.save(exampleTitle)

        var numberOfTitles = subscribedAnimeRepository.count()
        assertEquals(4, numberOfTitles)

        subscribedAnimeDataLoader.prepareDataOnStartup()

        numberOfTitles = subscribedAnimeRepository.count()
        assertEquals(3, numberOfTitles)
    }
}
