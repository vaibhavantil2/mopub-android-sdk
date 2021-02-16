// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Dips;
import com.mopub.mobileads.resource.CtaButtonDrawable;
import com.mopub.mobileads.resource.DrawableConstants;

public class VideoCtaButtonWidget extends ImageView {
    @NonNull private CtaButtonDrawable mCtaButtonDrawable;
    @NonNull private final RelativeLayout.LayoutParams mLayoutParams;

    private boolean mIsVideoSkippable;
    private boolean mIsVideoComplete;
    private boolean mHasCompanionAd;
    private boolean mHasClickthroughUrl;

    public VideoCtaButtonWidget(@NonNull final Context context, final boolean hasCompanionAd,
                                final boolean hasClickthroughUrl) {
        super(context);

        mHasCompanionAd = hasCompanionAd;
        mHasClickthroughUrl = hasClickthroughUrl;

        setId(View.generateViewId());

        final int width = Dips.dipsToIntPixels(DrawableConstants.CtaButton.WIDTH_DIPS, context);
        final int height = Dips.dipsToIntPixels(DrawableConstants.CtaButton.HEIGHT_DIPS, context);
        final int margin = Dips.dipsToIntPixels(DrawableConstants.CtaButton.MARGIN_DIPS, context);

        mCtaButtonDrawable = new CtaButtonDrawable(context);
        setImageDrawable(mCtaButtonDrawable);

        // portrait layout: placed bottom-right corner of screen
        mLayoutParams = new RelativeLayout.LayoutParams(width, height);
        mLayoutParams.setMargins(margin, margin, margin, margin);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        updateLayoutAndVisibility();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateLayoutAndVisibility();
    }

    void updateCtaText(@NonNull final String customCtaText) {
        mCtaButtonDrawable.setCtaText(customCtaText);
    }

    void notifyVideoSkippable() {
        mIsVideoSkippable = true;
        updateLayoutAndVisibility();
    }

    void notifyVideoComplete() {
        mIsVideoSkippable = true;
        mIsVideoComplete = true;
        updateLayoutAndVisibility();
    }

    void notifyVideoClickable() {
        mIsVideoComplete = true;
        updateLayoutAndVisibility();
    }

    private void updateLayoutAndVisibility() {
        // If the video does not have a clickthrough url, never show the CTA button
        if (!mHasClickthroughUrl) {
            setVisibility(View.GONE);
            return;
        }

        // If video is not skippable yet, do not show CTA button
        if (!mIsVideoSkippable) {
            setVisibility(View.INVISIBLE);
            return;
        }

        // If video has finished playing and there's a companion ad, do not show CTA button
        if (mIsVideoComplete && mHasCompanionAd) {
            setVisibility(View.GONE);
            return;
        }

        setLayoutParams(mLayoutParams);
        setVisibility(View.VISIBLE);
    }

    // for testing
    @Deprecated
    @VisibleForTesting
    String getCtaText() {
        return mCtaButtonDrawable.getCtaText();
    }
}
