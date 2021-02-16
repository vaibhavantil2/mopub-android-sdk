// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.simpleadsdemo;

import androidx.annotation.NonNull;

import com.mopub.common.Preconditions;
import com.mopub.common.SdkConfiguration;

class SampleActivityUtils {
    static void addDefaultNetworkConfiguration(@NonNull final SdkConfiguration.Builder builder) {
        Preconditions.checkNotNull(builder);

        // We have no default networks to initialize
    }
}
