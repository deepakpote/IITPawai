package net.mavericklabs.mitra.database.model;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/2/16.
 */

public class DbGrade extends RealmObject{
    private String gradeCommonCode;

    public DbGrade(String gradeCommonCode) {
        this.gradeCommonCode = gradeCommonCode;
    }

    public DbGrade() {
    }

    public String getGradeCommonCode() {
        return gradeCommonCode;
    }

    public void setGradeCommonCode(String gradeCommonCode) {
        this.gradeCommonCode = gradeCommonCode;
    }
}
