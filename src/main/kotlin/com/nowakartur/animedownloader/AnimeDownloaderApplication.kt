package com.nowakartur.animedownloader

import com.nowakartur.animedownloader.gogoanime.GogoanimeScraperService
import com.nowakartur.animedownloader.subsciption.SubscribedAnimeService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnimeDownloaderApplication(
    private val gogoanimeScraperService: GogoanimeScraperService,
    private val subscribedAnimeService: SubscribedAnimeService,
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {

//        logger.info("Searching for all subscribed anime.")
//
//        val subscribedAnime = subscribedAnimeService.findAllSubscribedAnime()
//
//        gogoanimeScraperService.downloadAnime(subscribedAnime)
    }
}

fun main(args: Array<String>) {
    runApplication<AnimeDownloaderApplication>(*args)
}
