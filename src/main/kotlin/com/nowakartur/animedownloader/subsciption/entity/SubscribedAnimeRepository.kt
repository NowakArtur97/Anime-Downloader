package com.nowakartur.animedownloader.subsciption.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

interface SubscribedAnimeRepository : JpaRepository<SubscribedAnimeEntity, UUID> {

    fun existsByTitle(title: String): Boolean

    fun findByStatusIsOrderByPriorityDesc(status: SubscribedAnimeStatus): List<SubscribedAnimeEntity>

    fun findByStatusIsAndLastModifiedDateBefore(
        status: SubscribedAnimeStatus,
        lastModifiedDate: LocalDateTime
    ): List<SubscribedAnimeEntity>

    @Transactional
    @Modifying
    @Query("update SubscribedAnimeEntity a set a.status = ?2 where a.status = ?1")
    fun updateAllStatusToStatus(originalStatus: SubscribedAnimeStatus, targetStatus: SubscribedAnimeStatus)
}

