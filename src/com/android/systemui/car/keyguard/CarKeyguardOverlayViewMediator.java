/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.car.keyguard;

import android.content.Context;

import com.android.systemui.car.users.CarSystemUIUserUtil;
import com.android.systemui.car.userswitcher.FullScreenUserSwitcherViewController;
import com.android.systemui.car.window.OverlayViewMediator;
import com.android.systemui.dagger.SysUISingleton;

import javax.inject.Inject;

/**
 * Manages events originating from the Keyguard service that cause Keyguard or other OverlayWindow
 * Components to appear or disappear.
 */
@SysUISingleton
public class CarKeyguardOverlayViewMediator implements OverlayViewMediator {

    private final Context mContext;
    private final CarKeyguardViewController mCarKeyguardViewController;
    private final FullScreenUserSwitcherViewController mFullScreenUserSwitcherViewController;

    @Inject
    public CarKeyguardOverlayViewMediator(
            Context context,
            CarKeyguardViewController carKeyguardViewController,
            FullScreenUserSwitcherViewController fullScreenUserSwitcherViewController
    ) {
        mContext = context;
        mCarKeyguardViewController = carKeyguardViewController;
        mFullScreenUserSwitcherViewController = fullScreenUserSwitcherViewController;
    }

    @Override
    public void registerListeners() {
        // TODO(b/269490856): consider removal of UserPicker carve-outs
        if (CarSystemUIUserUtil.isMUMDSystemUI()) {
            // TODO(b/258238612): update logic to stop passenger users
            mCarKeyguardViewController.registerOnKeyguardCancelClickedListener(
                    () -> CarSystemUIUserUtil.launchUserPicker(mContext));
        } else {
            mCarKeyguardViewController.registerOnKeyguardCancelClickedListener(
                    mFullScreenUserSwitcherViewController::start);
        }
    }

    @Override
    public void setUpOverlayContentViewControllers() {
        // no-op
    }
}
