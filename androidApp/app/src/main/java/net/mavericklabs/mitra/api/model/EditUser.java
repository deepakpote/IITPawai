package net.mavericklabs.mitra.api.model;

/**
 * Created by amoghpalnitkar on 12/12/16.
 */

public class EditUser {
    private String userID;
    private String userName;
    private String phoneNumber;
    private String udiseCode;
    private Integer userTypeCodeID;
    private Integer districtCodeID;
    private Integer preferredLanguageCodeID;
    private String subjectCodeIDs;
    private String skillCodeIDs;
    private String gradeCodeIDs;
    private String topicCodeIDs;

    public EditUser(String userID, String userName, String phoneNumber, Integer userTypeCodeID, Integer districtCodeID) {
        this.userID = userID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userTypeCodeID = userTypeCodeID;
        this.districtCodeID = districtCodeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUdiseCode() {
        return udiseCode;
    }

    public void setUdiseCode(String udiseCode) {
        this.udiseCode = udiseCode;
    }

    public Integer getUserTypeCodeID() {
        return userTypeCodeID;
    }

    public void setUserTypeCodeID(Integer userTypeCodeID) {
        this.userTypeCodeID = userTypeCodeID;
    }

    public Integer getDistrictCodeID() {
        return districtCodeID;
    }

    public void setDistrictCodeID(Integer districtCodeID) {
        this.districtCodeID = districtCodeID;
    }

    public Integer getPreferredLanguageCodeID() {
        return preferredLanguageCodeID;
    }

    public void setPreferredLanguageCodeID(Integer preferredLanguageCodeID) {
        this.preferredLanguageCodeID = preferredLanguageCodeID;
    }

    public String getSubjectCodeIDs() {
        return subjectCodeIDs;
    }

    public void setSubjectCodeIDs(String subjectCodeIDs) {
        this.subjectCodeIDs = subjectCodeIDs;
    }

    public String getSkillCodeIDs() {
        return skillCodeIDs;
    }

    public void setSkillCodeIDs(String skillCodeIDs) {
        this.skillCodeIDs = skillCodeIDs;
    }

    public String getGradeCodeIDs() {
        return gradeCodeIDs;
    }

    public void setGradeCodeIDs(String gradeCodeIDs) {
        this.gradeCodeIDs = gradeCodeIDs;
    }

    public String getTopicCodeIDs() {
        return topicCodeIDs;
    }

    public void setTopicCodeIDs(String topicCodeIDs) {
        this.topicCodeIDs = topicCodeIDs;
    }
}
