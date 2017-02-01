/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vishakha on 24/11/16.
 */

public class MitraSharedPreferences {

    public static <T extends Long> void saveToPreferences(Context context, String preferenceName, T preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(preferenceName, (Long) preferenceValue);
        editor.apply();
    }

    public static <T extends Integer> void saveToPreferences(Context context, String preferenceName, T preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(preferenceName, (Integer) preferenceValue);
        editor.apply();
    }

    public static <T extends String> void saveToPreferences(Context context, String preferenceName, T preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, (String) preferenceValue);
        editor.apply();
    }

    public static <T extends Boolean> void saveToPreferences(Context context, String preferenceName, T preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, (Boolean) preferenceValue);
        editor.apply();
    }

    public static <T extends String> String readFromPreferences(Context context, String preferenceName, T defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static <T extends Integer> Integer readFromPreferences(Context context, String preferenceName, T defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(preferenceName, defaultValue);
    }

    public static <T extends Long> Long readFromPreferences(Context context, String preferenceName, T defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(preferenceName, (Long) defaultValue);
    }

    public static <T extends Boolean> Boolean readFromPreferences(Context context, String preferenceName, T defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferenceName, (Boolean) defaultValue);
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    public static void removeFromPreferences(Context context, String preferenceName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(preferenceName);
        editor.apply();
    }

}

