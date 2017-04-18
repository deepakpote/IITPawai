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

package net.mavericklabs.mitra.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.StringUtils;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by vishakha on 18/04/17.
 */

@RealmClass
public class Chapter extends RealmObject {

    @SerializedName("chapter")
    @PrimaryKey
    private String chapterID;

    @SerializedName("chapterEng")
    private String chapterEnglish;

    @SerializedName("chapterMar")
    private String chapterMarathi;

    public String getChapterID() {
        return chapterID;
    }

    public String getChapterEnglish() {
        return chapterEnglish;
    }

    public String getChapterMarathi() {
        return chapterMarathi;
    }

    public String getChapterForCurrentLocale() {
        int languageCode = LanguageUtils.getCurrentLanguage();

        String currentLocale = CommonCodeUtils.getObjectFromCode(languageCode).getCodeNameEnglish();

        if(currentLocale.equals("English")) {
            return getChapterEnglish();
        } else if(currentLocale.equals("Marathi") && !StringUtils.isEmpty(getChapterMarathi())) {
            return getChapterMarathi();
        }

        return getChapterEnglish();
    }
}
