package com.nowakartur.animedownloader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnimeDownloaderApplication

fun main(args: Array<String>) {
    runApplication<AnimeDownloaderApplication>(*args)
}
