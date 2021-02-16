// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mopub.common.VisibleForTesting;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class exists to wrap InetAddress static calls since java.net classes cannot be mocked
 */
public class InetAddressUtils {
    private static InetAddress sMockInetAddress = null;

    @NonNull
    public static InetAddress getInetAddressByName(@Nullable final String host) throws UnknownHostException {
        if (sMockInetAddress != null) {
            return sMockInetAddress;
        }
        return InetAddress.getByName(host);
    }

    private InetAddressUtils() {
    }

    @Deprecated
    @VisibleForTesting
    static void setMockInetAddress(InetAddress inetAddress) {
        sMockInetAddress = inetAddress;
    }
}
