// Copyright 2018-2021 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// https://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mopub.common.CloseableLayout;
import com.mopub.common.test.support.SdkTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mopub.mobileads.AdData.DEFAULT_DURATION_FOR_CLOSE_BUTTON_MILLIS;
import static com.mopub.mobileads.AdData.MILLIS_IN_SECOND;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(SdkTestRunner.class)
public class FullscreenAdControllerTest {
    private static final String EXPECTED_HTML_DATA = "htmlData";
    private static final int REWARDED_DURATION_IN_SECONDS = 25;
    private static final int SHOW_CLOSE_BUTTON_DELAY = REWARDED_DURATION_IN_SECONDS * MILLIS_IN_SECOND;
    private static final String COMPANION_RESOURCE = "resource";
    private static final int COMPANION_WIDTH = 300;
    private static final int COMPANION_HEIGHT = 250;
    private static final String COMPANION_CLICKTHROUGH_URL = "clickthrough";
    private static final int VIDEO_DURATION_MS = 29875;

    private Activity activity;
    private long broadcastIdentifier;
    private AdData adData;
    private FullscreenAdController subject;
    private VastVideoConfig vastVideoConfig;
    private VastCompanionAdConfig vastCompanionAdConfig;
    private Set<VastCompanionAdConfig> vastCompanionAdConfigs;
    private List<VastTracker> companionClickTrackers;
    private List<VastTracker> companionCreativeViewTrackers;

    @Mock
    Intent mockIntent;
    @Mock
    Bundle mockBundle;

    @Before
    public void setUp() throws Exception {

        activity = spy(Robolectric.buildActivity(Activity.class).create().get());

        companionClickTrackers = new ArrayList<>();
        companionClickTrackers.add(new VastTracker("click1", VastTracker.MessageType.TRACKING_URL, false));
        companionClickTrackers.add(new VastTracker("click2", VastTracker.MessageType.TRACKING_URL, false));
        companionCreativeViewTrackers = new ArrayList<>();
        companionCreativeViewTrackers.add(new VastTracker("companion_view1", VastTracker.MessageType.TRACKING_URL, false));
        companionCreativeViewTrackers.add(new VastTracker("companion_view2", VastTracker.MessageType.TRACKING_URL, false));

        VastResource vastResource = new VastResource(
                COMPANION_RESOURCE,
                VastResource.Type.BLURRED_LAST_FRAME,
                VastResource.CreativeType.IMAGE,
                COMPANION_WIDTH,
                COMPANION_HEIGHT);
        vastCompanionAdConfig = new VastCompanionAdConfig(
                COMPANION_WIDTH,
                COMPANION_HEIGHT,
                vastResource,
                COMPANION_CLICKTHROUGH_URL,
                companionClickTrackers,
                companionCreativeViewTrackers,
                null);
        vastVideoConfig = new VastVideoConfig();
        vastVideoConfig.setNetworkMediaFileUrl("video_url");
        vastVideoConfig.setDiskMediaFileUrl("disk_video_path");
        vastVideoConfig.addVastCompanionAdConfig(vastCompanionAdConfig);
        vastCompanionAdConfigs = new HashSet<>();
        vastCompanionAdConfigs.add(vastCompanionAdConfig);
        broadcastIdentifier = 112233;
        adData = new AdData.Builder()
                .adPayload(EXPECTED_HTML_DATA)
                .broadcastIdentifier(broadcastIdentifier)
                .rewardedDurationSeconds(REWARDED_DURATION_IN_SECONDS)
                .vastVideoConfig(vastVideoConfig.toJsonString())
                .build();

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);
    }

    @Test
    public void constructor_withRewardedTrue_shouldInitializeShowCloseButtonDelay() {
        adData.setRewarded(true);

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        assertThat(subject.getShowCloseButtonDelayMillis()).isEqualTo(SHOW_CLOSE_BUTTON_DELAY);
    }

    @Test
    public void constructor_whenRewardedDurationIsNegative_shouldUseDefaultRewardedDuration() {
        adData.setRewarded(true);
        adData.setRewardedDurationSeconds(-3);

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        assertThat(subject.getShowCloseButtonDelayMillis())
                .isEqualTo(DEFAULT_DURATION_FOR_CLOSE_BUTTON_MILLIS);
    }

    @Test
    public void constructor_whenRewardedDurationIsLongerThanDefault_shouldUseRewardedDuration() {
        adData.setRewarded(true);
        adData.setRewardedDurationSeconds(90);

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        assertThat(subject.getShowCloseButtonDelayMillis()).isEqualTo(90 * 1000);
    }

    @Test
    public void constructor_shouldSetCloseableLayoutToVisible() {
        assertThat(subject.getCloseableLayout().isCloseVisible()).isTrue();
    }

    @Test
    public void constructor_withRewardedTrue_shouldSetCloseableLayoutToInvisible_shouldSetBackButtonEnabledFalse() {
        adData.setRewarded(true);

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        assertThat(subject.getCloseableLayout().isCloseVisible()).isFalse();
        assertThat(subject.backButtonEnabled()).isFalse();
    }

    @Test
    public void constructor_withRewardedFalse_shouldSetCloseableLayoutToVisible_shouldSetBackButtonEnabledTrue() {
        adData.setRewarded(false);

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        assertThat(subject.getCloseableLayout().isCloseVisible()).isTrue();
        assertThat(subject.backButtonEnabled()).isTrue();
    }

    @Test
    public void constructor_shouldInitializeRadialCountdownWidget_shouldInitializeCountdownRunnable() {
        adData.setRewarded(true);

        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        RadialCountdownWidget radialCountdownWidget = subject.getRadialCountdownWidget();
        assertThat(subject.isCalibrationDone()).isEqualTo(true);
        assertThat(radialCountdownWidget.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(radialCountdownWidget.getImageViewDrawable().getInitialCountdownMilliseconds())
                .isEqualTo(SHOW_CLOSE_BUTTON_DELAY);
        assertThat(subject.getCountdownRunnable()).isNotNull();
    }

    @Test
    public void pause_shouldStopRunnables() {
        adData.setRewarded(true);
        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);
        subject.resume();

        subject.pause();

        assertThat(subject.getCountdownRunnable().isRunning()).isFalse();
    }

    @Test
    public void resume_shouldStartRunnables() {
        adData.setRewarded(true);
        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        subject.resume();

        assertThat(subject.getCountdownRunnable().isRunning()).isTrue();
    }

    @Test
    public void destroy_shouldStopRunnables() {
        adData.setRewarded(true);
        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);
        subject.resume();

        subject.destroy();

        assertThat(subject.getCountdownRunnable().isRunning()).isFalse();
    }

    @Test
    public void showCloseButton_shouldToggleVisibilityStatesAndFireEvents() {
        adData.setRewarded(true);
        subject = new FullscreenAdController(activity, mockBundle, mockIntent, adData);

        RadialCountdownWidget radialCountdownWidget = subject.getRadialCountdownWidget();

        assertThat(subject.getCloseableLayout().isCloseVisible()).isFalse();
        assertThat(radialCountdownWidget.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(subject.isShowCloseButtonEventFired()).isFalse();
        assertThat(subject.isRewarded()).isFalse();
        subject.resume();

        subject.showCloseButton();

        assertThat(subject.getCloseableLayout().isCloseVisible()).isTrue();
        assertThat(radialCountdownWidget.getVisibility()).isEqualTo(View.GONE);
        assertThat(subject.isShowCloseButtonEventFired()).isTrue();
        assertThat(subject.isRewarded()).isTrue();
    }

    @Test
    public void useCustomCloseChangedTrue_withRewardedFalse_shouldHideCloseButton() {
        final CloseableLayout mockCloseableLayout = mock(CloseableLayout.class);
        subject.setCloseableLayout(mockCloseableLayout);

        subject.useCustomCloseChanged(true);

        verify(mockCloseableLayout).setCloseVisible(false);
    }

    @Test
    public void useCustomCloseChangedTrue_withRewardedTrue_shouldDoNothing() {
        final CloseableLayout mockCloseableLayout = mock(CloseableLayout.class);
        subject.setCloseableLayout(mockCloseableLayout);
        adData.setRewarded(true);

        subject.useCustomCloseChanged(true);

        verify(mockCloseableLayout, never()).setCloseVisible(false);
    }

    @Test
    public void useCustomCloseChangedFalse_withShowCloseButtonEventFired_shouldShowCloseButton() {
        final CloseableLayout mockCloseableLayout = mock(CloseableLayout.class);
        subject.setCloseableLayout(mockCloseableLayout);
        subject.showCloseButton();
        subject.useCustomCloseChanged(true);

        subject.useCustomCloseChanged(false);

        verify(mockCloseableLayout).setCloseVisible(false);
        verify(mockCloseableLayout, times(2)).setCloseVisible(true);
    }

    @Test
    public void onCompanionAdReady_withVastCompanionAdConfig_withBlurredLastVideoFrame_shouldSetUpCompanionAd() {
        subject.onCompanionAdsReady(vastCompanionAdConfigs, VIDEO_DURATION_MS);

        ImageView blurredLastVideoFrameImageView = subject.getImageView();
        assertThat(blurredLastVideoFrameImageView.getParent()).isEqualTo(null);
        assertThat(blurredLastVideoFrameImageView.getVisibility()).isEqualTo(View.VISIBLE);
        ShadowView blurredLastVideoFrameImageViewShadow = shadowOf(blurredLastVideoFrameImageView);
        // This has been changed for the new player which allows a click on the blurred frame
        assertThat(blurredLastVideoFrameImageViewShadow.getOnClickListener()).isNotNull();
    }

    @Test
    public void destroy_withBlurLastVideoFrameTaskStillPending_shouldCancelTask() {

        VastVideoBlurLastVideoFrameTask mockBlurLastVideoFrameTask = mock(VastVideoBlurLastVideoFrameTask.class);
        when(mockBlurLastVideoFrameTask.getStatus()).thenReturn(AsyncTask.Status.PENDING);
        subject.setBlurLastVideoFrameTask(mockBlurLastVideoFrameTask);

        subject.destroy();

        verify(mockBlurLastVideoFrameTask).cancel(true);
    }

    @Test
    public void destroy_withBlurLastVideoFrameTaskFinished_shouldCancelTask() {

        VastVideoBlurLastVideoFrameTask mockBlurLastVideoFrameTask = mock(VastVideoBlurLastVideoFrameTask.class);
        when(mockBlurLastVideoFrameTask.getStatus()).thenReturn(AsyncTask.Status.FINISHED);
        subject.setBlurLastVideoFrameTask(mockBlurLastVideoFrameTask);

        subject.destroy();

        verify(mockBlurLastVideoFrameTask).cancel(anyBoolean());
    }
}
