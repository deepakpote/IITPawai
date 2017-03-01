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

import java.util.ArrayList;
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

    public static List<CommonCode> getFileTypes() {
        return  Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                        CommonCodeGroup.FILE_TYPES).findAll();
    }


    public static CommonCode getDepartmentAtPosition(int position) {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                        CommonCodeGroup.DEPARTMENT_TYPES).findAll();
        return contentTypeResult.get(position);

    }
    public static int getDepartmentTypeCount() {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                        CommonCodeGroup.DEPARTMENT_TYPES).findAll();
        return contentTypeResult.size();

    }

    public static List<CommonCode> getDepartmentTypes() {
        return  Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                CommonCodeGroup.DEPARTMENT_TYPES).findAll();
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

    public static List<CommonCode> getAppLanguages() {
        return Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                CommonCodeGroup.APP_LANGUAGE).findAll();


    }
    public static List<CommonCode> getContentLanguages() {
        return Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeGroupID",
                CommonCodeGroup.CONTENT_LANGUAGE).findAll();


    }


    public static CommonCode getObjectFromCode(Integer code) {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeID",
                        code).findAll();
        return contentTypeResult.get(0);

    }

    public static Integer getAppLanguageCode(String language) {
        RealmResults<CommonCode> contentTypeResult =
                Realm.getDefaultInstance().where(CommonCode.class).equalTo("codeNameEnglish",
                        language).equalTo("codeGroupID", CommonCodeGroup.APP_LANGUAGE) .findAll();
        return contentTypeResult.get(0).getCodeID();

    }

    public static String getCommonCodeCommaSeparatedList(List<CommonCode> codes) {
        List<Integer> commonCodeStringList = new ArrayList<>();
        if(codes.size() > 0) {
            for (CommonCode code : codes) {
                commonCodeStringList.add(code.getCodeID());
            }

            return StringUtils.stringify(commonCodeStringList);
        }

        return "";

    }

    public static CommonCode getDepartmentCode(int position) {
        if(position == 0) {
            return CommonCodeUtils.getDepartmentAtPosition(position);
        }
        if(position == 1) {
            return new CommonCode(0,0, "Saved", "जतन केलेले", 0);
        }

        return CommonCodeUtils.getDepartmentAtPosition(position - 1);
    }
}
