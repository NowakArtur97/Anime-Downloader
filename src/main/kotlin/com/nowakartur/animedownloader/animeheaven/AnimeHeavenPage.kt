package com.nowakartur.animedownloader.animeheaven

import com.nowakartur.animedownloader.animeheaven.AnimeHeavenStyles.DOWNLOAD_BUTTON_CLASS
import com.nowakartur.animedownloader.animeheaven.AnimeHeavenStyles.EPISODE_CLASS
import com.nowakartur.animedownloader.animeheaven.AnimeHeavenStyles.EPISODE_NUMBER_CLASS
import com.nowakartur.animedownloader.animeheaven.AnimeHeavenStyles.EPISODE_NUMBER_SELECTOR
import com.nowakartur.animedownloader.animeheaven.AnimeHeavenStyles.TITLE_CLASS
import com.nowakartur.animedownloader.constant.HtmlConstants.ANCHOR_TAG
import com.nowakartur.animedownloader.selenium.SeleniumUtil
import org.jsoup.Jsoup
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

object AnimeHeavenPage {

    // TODO: refactor with DownloadPage?
    fun getAllAnimeDownloadInfo(animeheavenPageUrl: String) = Jsoup
        .connect(animeheavenPageUrl)
        .get()
        .body()
        .getElementsByClass(EPISODE_CLASS)
        .map { element ->
            val title = element.getElementsByClass(TITLE_CLASS)
                .first()!!
                .text()
            val episodeNumber = element.getElementsByClass(EPISODE_NUMBER_CLASS)
                .first()!!
                .text()
                .replace("ch", "")
                .replace("un", "")
                .replace("raw", "")
                .toInt()
            val url = element.getElementsByTag(ANCHOR_TAG)
                .last()!!
                .absUrl("href")
            AnimeHeavenDownloadInfo(title, episodeNumber, url);
        }

    fun connectToEpisodePage(webDriver: RemoteWebDriver, url: String) {
        webDriver.get(url)
    }

    fun selectNewestEpisode(webDriver: RemoteWebDriver) {
        webDriver.findElements(By.cssSelector(EPISODE_NUMBER_SELECTOR))
            .first()
            .click()
    }

    fun downloadEpisode(webDriver: RemoteWebDriver) {
        SeleniumUtil.waitFor(webDriver, By.className(DOWNLOAD_BUTTON_CLASS))
        webDriver.findElements(By.className(DOWNLOAD_BUTTON_CLASS))
            .last()
            .click()
    }
}
