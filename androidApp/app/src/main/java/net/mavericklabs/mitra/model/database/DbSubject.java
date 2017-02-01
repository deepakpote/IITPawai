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

package net.mavericklabs.mitra.model.database;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/2/16.
 */

public class DbSubject extends RealmObject {
    private Integer subjectCommonCode;

    public DbSubject() {
        //compulsory contructor
    }

    public DbSubject(Integer subjectCommonCode) {
        this.subjectCommonCode = subjectCommonCode;
    }

    public Integer getSubjectCommonCode() {
        return subjectCommonCode;
    }

    public void setSubjectCommonCode(Integer subjectCommonCode) {
        this.subjectCommonCode = subjectCommonCode;
    }
}
