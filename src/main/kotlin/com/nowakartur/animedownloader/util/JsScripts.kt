package com.nowakartur.animedownloader.util

object JsScripts {

    const val CLICK_SCRIPT = "arguments[0].click();"

    const val DOWNLOAD_PROGRESS_VALUE_SCRIPT =
        """return document.querySelector('downloads-manager')"
                ".shadowRoot"
                ".querySelector('#downloadsList downloads-item')"
                ".shadowRoot"
                ".querySelector('#progress')"
                ".value"""
}
