package net.mavericklabs.mitra.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class UserDetailUtils {
    public static String UNVERIFIED_PHONE_NUMBER = "-1";

    public static void saveUserId(String id , Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id",id);
        editor.apply();
    }

    public static String getUserId(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //TODO
        return "4";
        //return preferences.getString("user_id", "");
    }

    public static void saveToken(String id , Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",id);
        editor.apply();
    }

    public static String getToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("token", "");
    }

    public static void saveMobileNumber(String mobileNumber , Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ph_number",mobileNumber);
        editor.apply();
    }

    public static String getMobileNumber(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("ph_number", "");
    }

    public static void setVerifiedMobileNumber(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("verified",value);
        editor.apply();
    }

    public static boolean isVerifiedMobileNumber(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("verified", false);
    }
}
