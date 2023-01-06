package com.nowakartur.animedownloader.csv

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class CsvDataLoader(@Value(value = "classpath:anime.csv") private val animeList: Resource) {

    fun loadData(): List<SubscribedAnimeEntity> {
        val reader = animeList.inputStream.bufferedReader()
        return reader.lineSequence()
            .drop(1) // skip headers
            .filter { it.isNotBlank() }
            .map { mapToEntity(it) }
            .toList()
    }

    private fun mapToEntity(it: String): SubscribedAnimeEntity {
        val (title, priority) = it.split(";", limit = 2)
        return SubscribedAnimeEntity(
            title,
            if (priority.isNotBlank()) SubscribedAnimePriority.valueOf(priority)
            else SubscribedAnimePriority.LOW
        )
    }
}
