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
 * Created by amoghpalnitkar on 12/12/16.
 */

public class EditUser {
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

    public EditUser(String userName, String phoneNumber, Integer userTypeCodeID, Integer districtCodeID) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userTypeCodeID = userTypeCodeID;
        this.districtCodeID = districtCodeID;
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
