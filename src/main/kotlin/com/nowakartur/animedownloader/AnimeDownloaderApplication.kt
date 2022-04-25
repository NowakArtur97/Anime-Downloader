package com.nowakartur.animedownloader

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnimeDownloaderApplication(private val scraperService : ScraperService) : CommandLineRunner {

	override fun run(vararg args: String) {
		scraperService.retrieveData()
	}
}

fun main(args: Array<String>) {
	runApplication<AnimeDownloaderApplication>(*args)
}
