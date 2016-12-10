package net.mavericklabs.mitra.database.model;

import java.util.List;

import io.realm.RealmList;
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
    private String preferredLanguage;
    private String profilePhotoPath;
    private RealmList<DbSubject> subjects;
    private RealmList<DbGrade> grades;

    public DbUser() {
        //default constructor required.
    }

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

    public RealmList<DbSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(RealmList<DbSubject> subjects) {
        this.subjects = subjects;
    }

    public RealmList<DbGrade> getGrades() {
        return grades;
    }

    public void setGrades(RealmList<DbGrade> grades) {
        this.grades = grades;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }
}
