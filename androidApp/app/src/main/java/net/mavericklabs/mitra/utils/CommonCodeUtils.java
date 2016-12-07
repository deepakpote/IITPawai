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

import net.mavericklabs.mitra.model.CommonCode;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vishakha on 01/12/16.
 */

public class CommonCodeUtils {

    public static CommonCode getFileTypeAtPosition(int position) {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                        CommonCodeGroup.FILE_TYPES).findAll();
        return contentTypeResult.get(position);

    }
    public static int getFileTypeCount() {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                        CommonCodeGroup.FILE_TYPES).findAll();
        return contentTypeResult.size();

    }

    public static List<CommonCode> getSubjects() {
        return Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                        CommonCodeGroup.SUBJECTS).findAll();


    }

    public static List<CommonCode> getGrades() {
        return Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                CommonCodeGroup.GRADES).findAll();


    }

    public static List<CommonCode> getTopics() {
        return Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                CommonCodeGroup.TOPICS).findAll();


    }

    public static List<CommonCode> getLanguages() {
        return Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                CommonCodeGroup.LANGUAGE).findAll();


    }

    public static CommonCode getObjectFromCode(String code) {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeID",
                        code).findAll();
        return contentTypeResult.get(0);

    }

    public static String getLanguageCode(String language) {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeNameEnglish",
                        language).equalTo("codeGroupID", CommonCodeGroup.LANGUAGE) .findAll();
        return contentTypeResult.get(0).getCodeID();

    }

}
