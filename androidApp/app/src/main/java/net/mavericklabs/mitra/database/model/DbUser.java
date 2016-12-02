package net.mavericklabs.mitra.database.model;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by amoghpalnitkar on 15/11/16.
 */

public class DbUser extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String userType;
    private String district;
    private String Udise;
    private List<String> subjects;
    private List<String> grades;

    public DbUser(String name, String userType, String district) {
        this.name = name;
        this.userType = userType;
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUdise() {
        return Udise;
    }

    public void setUdise(String udise) {
        Udise = udise;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }
}
