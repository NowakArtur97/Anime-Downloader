package com.nowakartur.animedownloader.download.streamsb

import com.nowakartur.animedownloader.constant.HtmlConstants.ANCHOR_TAG

object StreamSbStyles {

    const val BEFORE_SIZE_TEXT = "("
    const val AFTER_SIZE_TEXT = " MB)"
    const val FILE_SIZES_CSS_SELECTOR = ".d-flex.align-items-center"

    const val DOWNLOAD_BUTTON_CLASS = "g-recaptcha"

    const val DOWNLOAD_LINK_CSS_SELECTOR = ".text-center.mb-4 $ANCHOR_TAG"
}
