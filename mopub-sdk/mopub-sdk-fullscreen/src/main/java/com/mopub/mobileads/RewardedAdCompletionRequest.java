// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads;

import android.content.Context;
import androidx.annotation.NonNull;

import com.mopub.network.MoPubRequest;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.Response;
import com.mopub.volley.RetryPolicy;
import com.mopub.volley.toolbox.HttpHeaderParser;

/**
 * The actual class making the rewarded ad completion request. Since we actually only care about the
 * status code of the request, that's the only thing that is delivered.
 */
public class RewardedAdCompletionRequest extends MoPubRequest<Integer> {

    public interface RewardedAdCompletionRequestListener extends Response.ErrorListener {
        void onResponse(Integer response);
    }

    @NonNull final RewardedAdCompletionRequestListener mListener;

    public RewardedAdCompletionRequest(@NonNull final Context context,
                                       @NonNull final String url,
                                       @NonNull final RetryPolicy retryPolicy,
                                       @NonNull final RewardedAdCompletionRequestListener listener) {
        super(context, url, listener);
        setShouldCache(false);
        setRetryPolicy(retryPolicy);
        mListener = listener;
    }

    @Override
    protected Response<Integer> parseNetworkResponse(final NetworkResponse networkResponse) {
        return Response.success(networkResponse.statusCode,
                HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(final Integer response) {
        mListener.onResponse(response);
    }
}
