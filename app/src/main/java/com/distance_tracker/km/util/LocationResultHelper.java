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

package com.distance_tracker.km.util;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import com.distance_tracker.km.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Class to process location results.
 */
public class LocationResultHelper {

    private final static String KEY_LOCATION_UPDATES_RESULT = "location_update_result";
    private static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults(Context context, String result) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, result)
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" : "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated, DateFormat.getDateTimeInstance().format(new Date()));
    }
}
