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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.CommonCode;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vishakha on 24/12/16.
 */

public class LanguageUtils {

    public static void setLocale(String languageShortCode, Context context) {
        Locale myLocale = new Locale(languageShortCode, "IN");
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }
        Logger.d(" set locale " + languageShortCode);
        Logger.d(" country default " + Locale.getDefault().getCountry());

        //Deprecated api - but still works. workaround is complicated
        res.updateConfiguration(conf, dm);
    }

    public static int getCurrentLanguage() {

        RealmResults<DbUser> dbUser = Realm.getDefaultInstance().where(DbUser.class).findAll();
        if(dbUser.size() > 0) {
            DbUser user = dbUser.get(0);
            return user.getPreferredLanguage();
        }
        return Constants.LanguageEnglish;

    }
}
