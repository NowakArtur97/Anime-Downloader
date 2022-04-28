package com.nowakartur.animedownloader.subsciption

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscribedAnimeRepository : JpaRepository<SubscribedAnimeEntity, UUID>
