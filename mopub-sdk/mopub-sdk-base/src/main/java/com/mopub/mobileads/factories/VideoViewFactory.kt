// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads.factories

import android.content.Context
import androidx.media2.widget.VideoView

open class VideoViewFactory {

    companion object {
        var instance = VideoViewFactory()

        fun create(context: Context): VideoView { return instance.internalCreate(context) }
    }

    open fun internalCreate(context: Context): VideoView { return VideoView(context) }
}
