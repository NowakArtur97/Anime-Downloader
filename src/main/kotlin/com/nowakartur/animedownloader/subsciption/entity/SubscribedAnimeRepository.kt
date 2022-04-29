package com.nowakartur.animedownloader.subsciption.entity

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscribedAnimeRepository : JpaRepository<SubscribedAnimeEntity, UUID> {

    fun findByStatusIsOrderByPriorityDesc(status: SubscribedAnimeStatus): List<SubscribedAnimeEntity>
}
