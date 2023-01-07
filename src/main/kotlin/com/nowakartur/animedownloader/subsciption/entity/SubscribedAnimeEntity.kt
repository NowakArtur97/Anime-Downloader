package com.nowakartur.animedownloader.subsciption.entity

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority.LOW
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeStatus.*
import org.hibernate.Hibernate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "subscribed_anime")
@EntityListeners(AuditingEntityListener::class)
data class SubscribedAnimeEntity(
    @Column(name = "title", nullable = false, unique = true)
    val title: String,

    @Column(name = "priority", nullable = false)
    var priority: SubscribedAnimePriority = LOW,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: SubscribedAnimeStatus = TO_DOWNLOAD,

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

    @LastModifiedDate
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

    fun changeStatusToToDownload() {
        status = TO_DOWNLOAD
    }

    fun changeStatusToInProgress() {
        status = IN_PROGRESS
    }

    fun changeStatusToDownloaded() {
        status = DOWNLOADED
    }
}
