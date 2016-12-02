package net.mavericklabs.mitra.database.model;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/2/16.
 */

public class DbSubject extends RealmObject {
    private String subjectCommonCode;

    public DbSubject() {
        //compulsory contructor
    }

    public DbSubject(String subjectCommonCode) {
        this.subjectCommonCode = subjectCommonCode;
    }

    public String getSubjectCommonCode() {
        return subjectCommonCode;
    }

    public void setSubjectCommonCode(String subjectCommonCode) {
        this.subjectCommonCode = subjectCommonCode;
    }
}
