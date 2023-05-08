# Anime Downloader

## Table of Contents

- [General info](#general-info)
- [Built With](#built-with)
- [Features](#features)
- [Status](#status)

## General info

The application allows you to automatically download anime episodes. For this purpose jsoup (to retrieve data that does
not require Javascript) and Selenium (to retrieve data that require Javascript) were used.

The application works as follows:

- when the application starts, the following actions are performed:
-- anime with title, minimum file size (default is 150MB) and priority are downloaded from `anime.csv` file
-- new titles are added
-- existing titles updated (priority and minimum file size)
-- anime from the database not found in the file are removed
-- and titles with `IN_PROGRESS` status are changed to `TO_DOWNLOAD`
- scheduler periodically downloads anime titles from database with status `IN_PROGRESS`
- sorts them by download priority (`HIGH`, `MEDIUM`, `LOW`)
- check if the website has new episodes of the selected titles
- if they are, the producer's thread finds download links from supported download servers
- then sorts them from best to worst quality and passes the data to the consumer's thread
- the consumer changes the status to `IN_PROGRESS` and starts the download
- in case of failure, it tries a specified number of times, and if there are too many failures, it changes the download server, if there is one
- after successful download, change status to `DOWNLOADED`
- the second scheduler cyclically changes the anime status from `DOWNLOADED` to `TO_DOWNLOAD`, so that when a new episode appears, it will be downloaded
- in case of failure a screenshot is taken and the `FAILED` status is set for some time, which means that the attempt to download the anime is skipped, and the next scheduler takes care of resetting this status

## Built With

- Java - 17
- Kotlin
- Spring (Boot, Data JPA) - 2.6.7
- jsoup - 1.15.3
- Selenium - 4.4.0
- Web Driver Manager - 5.3.0
- H2 - 2.1.214
- jUnit5 - 5.8.2
- Yandex QATools AShot WebDriver Utility - 1.5.4

## Features

- Cyclic download of data about new anime episodes
- Searching for download links from supported download servers
- Saving the status of an anime
- Downloading episodes taking into account the download priority
- Saving anime titles in the database at the start of the application
- Retrying, changing download servers and taking screenshots in case of download failures
- Using producer and consumer pattern

## Status

Project is: `maintained`

*Please bear in mind the production of this repository is for educational purposes, the author does not take any
responsibility for those who choose to actually use this repository.*
