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

package net.mavericklabs.mitra.model.api;

/**
 * Created by amoghpalnitkar on 12/9/16.
 */

public class LoginUser {
    private String userName;
    private String emailID ;
    private String gradeCodeIDs;
    private String subjectCodeIDs;
    private Integer district;
    private String photoUrl;
    private Integer preferredLanguage ;
    private String topicCodeIDs;
    private String userID ;
    private String skillCodeIDs;
    private Integer userType;
    private String phoneNumber;
    private String udiseCode;
    private Integer department;

    public LoginUser(String userName, Integer userType, Integer district) {
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

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(Integer preferredLanguage) {
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
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

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }
}
