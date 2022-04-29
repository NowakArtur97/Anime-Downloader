package com.nowakartur.animedownloader.subsciption

import org.hibernate.Hibernate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


enum class SubscribedAnimeStatus {
    TO_DOWNLOAD, IN_PROGRESS, DOWNLOADED
}

enum class SubscribedAnimePriority(val value: Int) {
    LOW(1), MEDIUM(2), HIGH(3)
}

@Entity
@Table(name = "subscribed_anime")
@EntityListeners(AuditingEntityListener::class)
data class SubscribedAnimeEntity(
    @Column(name = "title", nullable = false, unique = true)
    val title: String,

    @Column(name = "priority", nullable = false)
    private val priority: SubscribedAnimePriority = SubscribedAnimePriority.MEDIUM,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private var status: SubscribedAnimeStatus = SubscribedAnimeStatus.TO_DOWNLOAD,

    @Transient
    var priorityValue: Int = priority.value,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private val id: Long = 0

    @CreatedDate
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null

    @CreatedDate
    @Column(name = "last_modified_date")
    var lastModifiedDate: LocalDateTime? = null

    @PostLoad
    fun setPriorityValue() {
        priorityValue = priority.value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SubscribedAnimeEntity

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , priority = $priority , status = $status , priorityValue = $priorityValue , createdAt = $createdDate , updatedAt = $lastModifiedDate )"
    }

    fun waitForDownload() {
        status = SubscribedAnimeStatus.TO_DOWNLOAD
    }

    fun startDownloading() {
        status = SubscribedAnimeStatus.IN_PROGRESS
    }

    fun finishDownloading() {
        status = SubscribedAnimeStatus.DOWNLOADED
    }
}
