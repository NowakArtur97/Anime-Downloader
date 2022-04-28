package com.nowakartur.animedownloader.subsciption

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

enum class SubscribedAnimeStatus {
    TO_DOWNLOAD, IN_PROGRESS, DOWNLOADED
}

@Entity
@Table(name = "subscribed_anime")
data class SubscribedAnimeEntity(
    @Column(name = "title", nullable = false, unique = true)
    val title: String,
    @Column(name = "priority", nullable = false)
    val priority: Int,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: SubscribedAnimeStatus = SubscribedAnimeStatus.TO_DOWNLOAD,
) {
    @Id
    @Column(name = "id")
    val id = UUID.randomUUID()

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    var lastModifiedDate: LocalDateTime? = null
}
