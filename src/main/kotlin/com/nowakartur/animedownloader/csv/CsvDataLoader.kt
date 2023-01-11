package com.nowakartur.animedownloader.csv

import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimeEntity
import com.nowakartur.animedownloader.subsciption.entity.SubscribedAnimePriority
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils

@Service
class CsvDataLoader(
    @Value("\${app.csv.file-name}") private val fileName: String,
    @Value("\${app.default-min-file-size}") private val defaultMinFileSize: String
) {

    fun loadData(): List<SubscribedAnimeEntity> {
        val animeListCsv = ResourceUtils.getFile("classpath:${fileName}")
        val reader = animeListCsv.inputStream().bufferedReader()
        return reader.lineSequence()
            .drop(1) // skip headers
            .filter { it.isNotBlank() }
            .map { mapToEntity(it) }
            .toList()
    }

    private fun mapToEntity(it: String): SubscribedAnimeEntity {
        val (title, priority, minFileSize) = it.split(";", limit = 3)
        return SubscribedAnimeEntity(
            title,
            if (priority.isNotBlank()) SubscribedAnimePriority.valueOf(priority)
            else SubscribedAnimePriority.LOW,
            minFileSize = (minFileSize.ifBlank { defaultMinFileSize }).toFloat(),
        )
    }
}
