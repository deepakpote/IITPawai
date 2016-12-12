package net.mavericklabs.mitra.api.model;

/**
 * Created by amoghpalnitkar on 12/9/16.
 */

public class LoginUser {
    private String userName;
    private String emailID ;
    private String gradeCodeIDs;
    private String subjectCodeIDs;
    private String district;
    private String photoUrl;
    private String preferredLanguage ;
    private String topicCodeIDs;
    private String userID ;
    private String skillCodeIDs;
    private String userType;
    private String phoneNumber;
    private String udiseCode;

    public LoginUser(String userName, String userType, String district) {
        this.userName = userName;
        this.userType = userType;
        this.district = district;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getGradeCodeIDs() {
        return gradeCodeIDs;
    }

    public void setGradeCodeIDs(String gradeCodeIDs) {
        this.gradeCodeIDs = gradeCodeIDs;
    }

    public String getSubjectCodeIDs() {
        return subjectCodeIDs;
    }

    public void setSubjectCodeIDs(String subjectCodeIDs) {
        this.subjectCodeIDs = subjectCodeIDs;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getTopicCodeIDs() {
        return topicCodeIDs;
    }

    public void setTopicCodeIDs(String topicCodeIDs) {
        this.topicCodeIDs = topicCodeIDs;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSkillCodeIDs() {
        return skillCodeIDs;
    }

    public void setSkillCodeIDs(String skillCodeIDs) {
        this.skillCodeIDs = skillCodeIDs;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
}
