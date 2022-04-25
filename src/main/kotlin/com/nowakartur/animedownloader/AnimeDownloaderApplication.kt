package com.nowakartur.animedownloader

import com.nowakartur.animedownloader.gogoanime.GogoanimeScraperService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnimeDownloaderApplication(private val gogoanimeScraperService: GogoanimeScraperService) : CommandLineRunner {

    override fun run(vararg args: String) {
        gogoanimeScraperService.scrapDownloadLinks()
    }
}

fun main(args: Array<String>) {
    runApplication<AnimeDownloaderApplication>(*args)
}
