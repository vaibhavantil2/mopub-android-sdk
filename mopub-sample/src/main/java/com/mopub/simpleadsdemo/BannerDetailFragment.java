// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.simpleadsdemo;

import com.mopub.mobileads.MoPubView;

public class BannerDetailFragment extends AbstractBannerDetailFragment {

    @Override
    public MoPubView.MoPubAdSize getAdSize() {
        return MoPubView.MoPubAdSize.HEIGHT_250;
    }
}
