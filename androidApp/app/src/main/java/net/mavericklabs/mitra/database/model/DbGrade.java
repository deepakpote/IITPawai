package net.mavericklabs.mitra.database.model;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/2/16.
 */

public class DbGrade extends RealmObject{
    private Integer gradeCommonCode;

    public DbGrade(Integer gradeCommonCode) {
        this.gradeCommonCode = gradeCommonCode;
    }

    public DbGrade() {
    }

    public Integer getGradeCommonCode() {
        return gradeCommonCode;
    }

    public void setGradeCommonCode(Integer gradeCommonCode) {
        this.gradeCommonCode = gradeCommonCode;
    }
}
