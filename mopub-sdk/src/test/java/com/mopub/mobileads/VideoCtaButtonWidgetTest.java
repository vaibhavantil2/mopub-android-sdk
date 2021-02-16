// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.mopub.common.test.support.SdkTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SdkTestRunner.class)
public class VideoCtaButtonWidgetTest {
    private Context context;
    private VideoCtaButtonWidget subject;

    @Before
    public void setUp() throws Exception {
        context = Robolectric.buildActivity(Activity.class).create().get();
    }

    @Test
    public void constructor_withCompanionAd_shouldBeInvisibleAndNotSetLayoutParams() throws Exception {
        subject = new VideoCtaButtonWidget(context, true, true);

        assertThat(subject.getVisibility()).isEqualTo(View.INVISIBLE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    @Test
    public void constructor_withoutCompanionAd_shouldBeInvisibleAndNotSetLayoutParams() throws Exception {
        subject = new VideoCtaButtonWidget(context, false, true);

        assertThat(subject.getVisibility()).isEqualTo(View.INVISIBLE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    @Test
    public void constructor_withCompanionAd_withNoClickthroughUrl_shouldBeGoneAndNotSetLayoutParams() throws Exception {
        subject = new VideoCtaButtonWidget(context, true, false);

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    @Test
    public void constructor_withoutCompanionAd_withNoClickthroughUrl_shouldBeGoneAndNotSetLayoutParams() throws Exception {
        subject = new VideoCtaButtonWidget(context, false, false);

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    // Video is skippable, has companion ad, has clickthrough url, CTA button initially invisible

    @Test
    public void notifyVideoSkippable_withCompanionAdAndInPortrait_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;
        subject = new VideoCtaButtonWidget(context, true, true);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    @Test
    public void notifyVideoSkippable_withCompanionAdAndInLandscape_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        subject = new VideoCtaButtonWidget(context, true, true);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    @Test
    public void notifyVideoSkippable_withCompanionAdAndOrientationUndefined_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_UNDEFINED;
        subject = new VideoCtaButtonWidget(context, true, true);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    // Video is skippable, no companion ad, has clickthrough url, CTA button initially invisible

    @Test
    public void notifyVideoSkippable_withoutCompanionAdAndInPortrait_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;
        subject = new VideoCtaButtonWidget(context, false, true);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    @Test
    public void notifyVideoSkippable_withoutCompanionAdAndInLandscape_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        subject = new VideoCtaButtonWidget(context, false, true);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    @Test
    public void notifyVideoSkippable_withoutCompanionAdAndOrientationUndefined_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_UNDEFINED;
        subject = new VideoCtaButtonWidget(context, false, true);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    // Video is complete, has companion ad, CTA button already visible

    @Test
    public void notifyVideoComplete_withCompanionAdAndInPortrait_shouldBeGoneAndNotChangeLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;
        subject = new VideoCtaButtonWidget(context, true, true);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    @Test
    public void notifyVideoComplete_withCompanionAdAndInLandscape_shouldBeGoneAndNotChangeLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        subject = new VideoCtaButtonWidget(context, true, true);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    @Test
    public void notifyVideoComplete_withCompanionAdAndOrientationUndefined_shouldBeGoneAndNotChangeLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_UNDEFINED;
        subject = new VideoCtaButtonWidget(context, true, true);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
        assertThat(subject.getLayoutParams()).isNull();
    }

    // Video is complete, no companion ad, has clickthrough url, CTA button already visible

    @Test
    public void notifyVideoComplete_withoutCompanionAdAndInPortrait_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;
        subject = new VideoCtaButtonWidget(context,false, true);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    @Test
    public void notifyVideoComplete_withoutCompanionAdAndInLandscape_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        subject = new VideoCtaButtonWidget(context, false, true);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    @Test
    public void notifyVideoComplete_withoutCompanionAdAndOrientationUndefined_shouldBeVisibleAndSetLayoutParams() throws Exception {
        context.getResources().getConfiguration().orientation = Configuration.ORIENTATION_UNDEFINED;
        subject = new VideoCtaButtonWidget(context, false, true);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.getLayoutParams()).isNotNull();
    }

    // No clickthrough url means never show cta button

    @Test
    public void notifyVideoSkippable_withoutClickthroughUrl_shouldBeGone() throws Exception {
        subject = new VideoCtaButtonWidget(context, true, false);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoSkippable();

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void notifyVideoComplete_withoutClickthroughUrl_shouldBeGone() throws Exception {
        subject = new VideoCtaButtonWidget(context, true, false);
        subject.setVisibility(View.VISIBLE);

        subject.notifyVideoComplete();

        assertThat(subject.getVisibility()).isEqualTo(View.GONE);
    }
}
