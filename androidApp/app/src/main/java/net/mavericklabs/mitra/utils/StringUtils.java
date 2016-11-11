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

import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by root on 3/11/16.
 */

public class StringUtils {


    /**
     * Java isEmpty function fails for null Strings.
     * Custom isEmpty function which returns true on NULL & BLANK strings
     *
     * @param str
     * @return true if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean equals(String str1 ,String str2) {
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        return str1.equals(str2);
    }


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
        return DateUtils.isToday(clickedDateTime);
    }

    public static boolean isThisYear(long clickedDateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy",Locale.US);
        Calendar calendar = Calendar.getInstance();
        String year = simpleDateFormat.format(new Date(clickedDateTime));
        return year.equals(String.valueOf(calendar.get(Calendar.YEAR)));
    }

    public static String getFilePathWithoutExtension(String realPath) {
        String pathWithoutExtension = null;
        int lastDotPosition = realPath.lastIndexOf('.');
        if( lastDotPosition > 0 ) {
            pathWithoutExtension = realPath.substring(0, lastDotPosition);
        }
        return pathWithoutExtension;
    }


    public static String getYear(long clickedDateTime) {
        if(clickedDateTime == 0){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(clickedDateTime);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }


    public static InputFilter getEmoticonFilter(){
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    int type = Character.getType(source.charAt(i));
                    //System.out.println("Type : " + type);
                    if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                        return "";
                    }
                }
                return null;
            }
        };
        return filter;
    }
    public static String timeFormat(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time){
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path){
        File file = new File(path);
        if(file.exists()){
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    public static String sanitizeNonAsciiUnicode(String str){
        return str.replaceAll("[^\\x00-\\x7F]", "");
    }

    public static String removeAllWhitespace(String str) {
        return str.replaceAll("\\s+","");
    }

}
