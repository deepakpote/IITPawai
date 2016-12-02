package net.mavericklabs.mitra.utils;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by amoghpalnitkar on 15/11/16.
 */

public class DateUtils {

    public static String parseDateTimeToHoursAndMinutes(String str) {
        if (!StringUtils.isEmpty(str)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
            SimpleDateFormat requiredDateFormat = new SimpleDateFormat("hh:mma",Locale.US);
            Date formattedDate = null;
            try {
                formattedDate = simpleDateFormat.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (requiredDateFormat.format(formattedDate) == null) {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
                try {
                    formattedDate = simpleDateFormat.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return requiredDateFormat.format(formattedDate);
        }
        return "Date unavailable";
    }

    public static long convertToMilliseconds(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMyy",Locale.US);
        long milliseconds = 0;
        try {
            Date newDate = simpleDateFormat.parse(date);
            milliseconds = newDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public static long convertToMilliseconds(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss",Locale.US);
        long milliseconds = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    public static long convertToMillisecondsFromServer(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        long milliseconds = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    public static long convertToMillisecondsFromServerUTC(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        long milliseconds = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    public static long convertToMillisecondsFromSqlite(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        long milliseconds = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    public static long convertToMillisecondsFromSqliteUTC(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        long milliseconds = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    public static String convertToDate(long milliSeconds, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(new Date(milliSeconds));
    }

    public static boolean isToday(long clickedDateTime) {
        return android.text.format.DateUtils.isToday(clickedDateTime);
    }

    public static boolean isThisYear(long clickedDateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy",Locale.US);
        Calendar calendar = Calendar.getInstance();
        String year = simpleDateFormat.format(new Date(clickedDateTime));
        return year.equals(String.valueOf(calendar.get(Calendar.YEAR)));
    }


    public static String timeFormat(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    @Nullable
    public static String getYear(long clickedDateTime) {
        if(clickedDateTime == 0){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(clickedDateTime);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

}
