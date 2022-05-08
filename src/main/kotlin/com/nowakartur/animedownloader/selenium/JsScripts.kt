package com.nowakartur.animedownloader.selenium

object JsScripts {

    const val CLICK_SCRIPT = "arguments[0].click();"

    const val DOWNLOAD_VIDEO_SCRIPT = """const link = document.querySelector("source").src;
            |const a = document.createElement("a"); 
            |a.href = link; 
            |a.download = "vid.mp4";
            |document.body.appendChild(a);
            |a.click();"""

    const val DOWNLOAD_PROGRESS_VALUE_SCRIPT =
        "return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#progress').value"
}
