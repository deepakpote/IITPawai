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

import com.google.gson.annotations.SerializedName;

import net.mavericklabs.mitra.utils.Logger;

import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vishakha on 22/11/16.
 */

public class CommonCode extends RealmObject{

    @PrimaryKey
    @SerializedName("codeID")
    private String codeID;

    @SerializedName("codeGroup")
    private String codeGroupID;

    @SerializedName("codeNameEn")
    private String codeNameEnglish;

    @SerializedName("codeNameMr")
    private String codeNameMarathi;

    @SerializedName("displayOrder")
    private int displayOder;

    public CommonCode() {
    }

    public CommonCode(String codeID, String codeGroupID, String codeNameEnglish, String codeNameMarathi, int displayOder) {
        this.codeID = codeID;
        this.codeGroupID = codeGroupID;
        this.codeNameEnglish = codeNameEnglish;
        this.codeNameMarathi = codeNameMarathi;
        this.displayOder = displayOder;
    }

    public String getCodeID() {
        return codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public String getCodeGroupID() {
        return codeGroupID;
    }

    public void setCodeGroupID(String codeGroupID) {
        this.codeGroupID = codeGroupID;
    }

    public String getCodeNameEnglish() {
        return codeNameEnglish;
    }

    public void setCodeNameEnglish(String codeNameEnglish) {
        this.codeNameEnglish = codeNameEnglish;
    }

    public String getCodeNameMarathi() {
        return codeNameMarathi;
    }

    public void setCodeNameMarathi(String codeNameMarathi) {
        this.codeNameMarathi = codeNameMarathi;
    }

    public int getDisplayOder() {
        return displayOder;
    }

    public void setDisplayOder(int displayOder) {
        this.displayOder = displayOder;
    }

    public String getCodeNameForCurrentLocale() {
        String currentLocale = Locale.getDefault().getLanguage();
        if(currentLocale.equals("en")) {
            return codeNameEnglish;
        } else if(currentLocale.equals("mr")) {
            return codeNameMarathi;
        }

        return codeNameEnglish;
    }
}
