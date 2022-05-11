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
        "return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#progress').value;"

    const val HAS_DOWNLOAD_STOPPED_DOWNLOAD_SCRIPT =
        "return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#tag').innerText.includes('Failed')"

    const val RESUME_DOWNLOAD_SCRIPT =
        "document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#pauseOrResume').click();"
}
