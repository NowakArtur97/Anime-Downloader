package com.nowakartur.animedownloader.subsciption

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*
import javax.persistence.*

enum class SubscribedAnimeStatus {
    TO_DOWNLOAD, IN_PROGRESS, DOWNLOADED
}

enum class SubscribedAnimePriority(val value: Int) {
    LOW(1), MEDIUM(2), HIGH(3)
}

@Entity
@Table(name = "subscribed_anime")
data class SubscribedAnimeEntity(
    @Column(name = "title", nullable = false, unique = true)
    val title: String,

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private val priority: SubscribedAnimePriority = SubscribedAnimePriority.MEDIUM,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: SubscribedAnimeStatus = SubscribedAnimeStatus.TO_DOWNLOAD,

    @Transient
    var priorityValue: Int = priority.value,
) {
    @Id
    @Column(name = "id")
    val id = UUID.randomUUID()

    @CreationTimestamp
    @Column(name = "created_date")
    val createdAt: Instant? = null

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    val updatedAt: Instant? = null

    @PostLoad
    fun setPriorityValue() {
        priorityValue = priority.value
    }
}
