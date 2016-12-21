package net.mavericklabs.mitra.utils;

import android.support.annotation.Nullable;

import java.text.DateFormat;
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

    public static Date convertToDate(String dateTime, String fromFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromFormat, Locale.US);
        try {
            return simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertToString(Date date, String newFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(newFormat, Locale.US);
        return dateFormat.format(date);
    }

    public static String convertToServerFormatFromDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);

        return df.format(date);
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
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
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

    public static void setTimeToBeginningOfMonth(Calendar calendar) {
        // get start of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setTimeToBeginningOfDay(calendar);
    }

    public static void setTimeToEndOfMonth(Calendar calendar) {
        // get start of the month
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
    }

    public static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

}
