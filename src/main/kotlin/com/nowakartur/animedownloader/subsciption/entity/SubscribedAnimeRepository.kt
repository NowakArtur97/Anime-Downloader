package com.nowakartur.animedownloader.subsciption.entity

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface SubscribedAnimeRepository : JpaRepository<SubscribedAnimeEntity, UUID> {

    fun existsByTitle(title: String): Boolean

    fun findByStatusIsOrderByPriorityDesc(status: SubscribedAnimeStatus): List<SubscribedAnimeEntity>

    fun findByStatusIsAndLastModifiedDateBefore(
        status: SubscribedAnimeStatus,
        lastModifiedDate: LocalDateTime
    ): List<SubscribedAnimeEntity>
}

