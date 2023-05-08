package com.nowakartur.animedownloader.subsciption.scheduler

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeRepository
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = ["app.csv.file-name=test-anime.csv", "app.scheduler.reset.number-of-days-after-to-reset=0"])
class SubscribedAnimeStatusResetSchedulerTest(
    @Autowired private val subscribedAnimeStatusResetScheduler: SubscribedAnimeStatusResetScheduler,
    @Autowired private val subscribedAnimeRepository: SubscribedAnimeRepository,
) {

    @AfterEach
    fun cleanDatabase() {
        subscribedAnimeRepository.deleteAll()
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when there is DOWNLOADED title should update status to TO_DOWNLOAD`() {
        val exampleTitle = subscribedAnimeRepository.findAll().first()
        exampleTitle.changeStatusToDownloaded()
        subscribedAnimeRepository.save(exampleTitle)

        subscribedAnimeStatusResetScheduler.resetStatuses()

        val findAll = subscribedAnimeRepository.findAll()
        assertTrue(findAll.none { it.status == SubscribedAnimeStatus.DOWNLOADED })
    }
}
