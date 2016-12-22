package net.mavericklabs.mitra.database.model;

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
