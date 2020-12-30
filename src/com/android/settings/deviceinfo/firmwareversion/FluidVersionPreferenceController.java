/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.android.settings.deviceinfo.firmwareversion;

import java.io.IOException;
import android.content.Context;
import android.os.SystemProperties;
import androidx.annotation.VisibleForTesting;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.utils.FluidSpecUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.widget.LayoutPreference;
import com.android.settingslib.Utils;
import com.android.settings.core.PreferenceControllerMixin;

import android.widget.TextView;

public class FluidVersionPreferenceController extends BasePreferenceController {

    @VisibleForTesting
    private static final String FLUID_VERSION_PROPERTY = "ro.fluid.build.version";
    private static final String FLUID_BUILD_TYPE = "ro.fluid.buildtype";

    @VisibleForTesting
    TextView mFluidVersionText;
    @VisibleForTesting
    TextView mFluidVersionFlavourText;
    @VisibleForTesting
    TextView mDeviceNameText;
    @VisibleForTesting
    TextView mCpuText;
    @VisibleForTesting
    TextView mScreenResText;
    @VisibleForTesting
    TextView mBatteryText;
    @VisibleForTesting
    TextView mRamText;
    @VisibleForTesting
    TextView mMaintainerText;

    private PreferenceFragmentCompat mHost;
    private LayoutPreference mFluidVersionLayoutPref;
    private Context mContext;

    public FluidVersionPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
        mContext = context;
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    public void setFragment(PreferenceFragmentCompat fragment) {
        mHost = fragment;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mFluidVersionLayoutPref = screen.findPreference(getPreferenceKey());
        mFluidVersionText = mFluidVersionLayoutPref.findViewById(R.id.fluid_version);
        mFluidVersionFlavourText = mFluidVersionLayoutPref.findViewById(R.id.fluid_version_flavour);
        mDeviceNameText = mFluidVersionLayoutPref.findViewById(R.id.device_name_text);
        mCpuText = mFluidVersionLayoutPref.findViewById(R.id.device_cpu_text);
        mScreenResText = mFluidVersionLayoutPref.findViewById(R.id.device_screen_res_text);
        mBatteryText = mFluidVersionLayoutPref.findViewById(R.id.device_battery_text);
        mRamText = mFluidVersionLayoutPref.findViewById(R.id.device_ram_text);
        mMaintainerText = mFluidVersionLayoutPref.findViewById(R.id.device_maintainer_text);

        UpdateFluidVersionPreference();
    }

    private void UpdateFluidVersionPreference() {
        // We split the different specs into different voids to make the code more organized.
        updateFluidVersionText();
        updateDeviceNameText();
        updateCpuText();
        updateScreenResText();
        updateBatteryText();
        updateRamText();
        updateMaintainerText();
    }

    private void updateFluidVersionText() {
        String fluidVer = SystemProperties.get(FLUID_VERSION_PROPERTY);
        String fluidType = SystemProperties.get(FLUID_BUILD_TYPE);
        String fluidTypeCapitalized = fluidType.substring(0, 1).toUpperCase() + fluidType.substring(1).toLowerCase();
        String[] fluidVerSeparated = fluidVer.split("-");

        if (!fluidVer.isEmpty() && !fluidType.isEmpty()) {
            mFluidVersionText.setText(fluidVerSeparated[0] + " " + fluidTypeCapitalized + " | ");
            mFluidVersionFlavourText.setText(fluidVerSeparated[1]);
        } else {
            mFluidVersionText.setText("");
            mFluidVersionFlavourText.setText(R.string.unknown);
        }
    }

    private void updateDeviceNameText() {
        mDeviceNameText.setText(FluidSpecUtils.getDeviceName());
    }

    private void updateBatteryText() {
        mBatteryText.setText(FluidSpecUtils.getBatteryCapacity(mContext) + " mAh");
    }

    private void updateCpuText() {
        mCpuText.setText(FluidSpecUtils.getProcessorModel());
    }

    private void updateScreenResText() {
        mScreenResText.setText(FluidSpecUtils.getScreenRes(mContext));
    }

    private void updateRamText() {
        mRamText.setText(String.valueOf(FluidSpecUtils.getTotalRAM())+ " GB");
    }
    private void updateMaintainerText() {
        mMaintainerText.setText(String.valueOf(FluidSpecUtils.getMaintainerName()));
    }

}
