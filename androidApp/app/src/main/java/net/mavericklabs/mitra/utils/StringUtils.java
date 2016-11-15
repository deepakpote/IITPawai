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

    public static String getFilePathWithoutExtension(String realPath) {
        String pathWithoutExtension = null;
        int lastDotPosition = realPath.lastIndexOf('.');
        if( lastDotPosition > 0 ) {
            pathWithoutExtension = realPath.substring(0, lastDotPosition);
        }
        return pathWithoutExtension;
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

    public static String sanitizeNonAsciiUnicode(String str){
        return str.replaceAll("[^\\x00-\\x7F]", "");
    }

    public static String removeAllWhitespace(String str) {
        return str.replaceAll("\\s+","");
    }

}
