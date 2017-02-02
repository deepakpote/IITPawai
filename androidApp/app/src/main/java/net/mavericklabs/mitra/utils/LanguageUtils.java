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

import net.mavericklabs.mitra.model.database.DbUser;
import net.mavericklabs.mitra.model.database.Language;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vishakha on 24/12/16.
 */

public class LanguageUtils {

    public static void setLocale(int languageCode, Context context) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DbUser> dbUser = realm.where(DbUser.class).findAll();
        if(dbUser.size() > 0) {
            DbUser user = dbUser.get(0);
            realm.beginTransaction();
            user.setPreferredLanguage(languageCode);
            realm.commitTransaction();
        }

        Language language = new Language();
        language.setAppLanguage(languageCode);

        realm.beginTransaction();

        //Delete existing rows in table
        realm.delete(Language.class);
        realm.copyToRealm(language);
        realm.commitTransaction();

        //Set app locale as well
        Locale myLocale;

        if(languageCode == Constants.AppLanguageEnglish) {
            myLocale = new Locale("en", "IN");
        } else {
            myLocale = new Locale("mr", "IN");
        }
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }
        Logger.d(" country default " + Locale.getDefault().getCountry());

        //Deprecated api - but still works. workaround is complicated
        res.updateConfiguration(conf, dm);
    }

    public static int getCurrentLanguage() {

        RealmResults<Language> language = Realm.getDefaultInstance().where(Language.class).findAll();
        if(language.size() > 0) {
            Language appLanguage = language.get(0);
            return appLanguage.getAppLanguage();
        }
        return Constants.AppLanguageEnglish;

    }

    public static Locale getCurrentLocale() {

        int languageCode = getCurrentLanguage();

        if(languageCode == Constants.AppLanguageEnglish) {
            return new Locale("en", "IN");
        } else {
            return new Locale("mr", "IN");
        }
    }
}
