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

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class RegisterUser {

    @SerializedName("userName")
    private String name;

    @SerializedName("otp")
    private String otp;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("district")
    private Integer district;

    @SerializedName("userType")
    private Integer userType;

    @SerializedName("udiseCode")
    private String udiseCode;

    @SerializedName("subjectCodeIDs")
    private String subjectCodeIds;

    @SerializedName("gradeCodeIDs")
    private String gradeCodeIds;

    @SerializedName("topicCodeIDs")
    private String topicCodeIds;

    @SerializedName("preferredLanguage")
    private Integer preferredLanguage;


    public RegisterUser(String userName, String otp, String phoneNumber, Integer districtCode, Integer userType, Integer preferredLanguage) {
        this.name = userName;
        this.otp = otp;
        this.phoneNumber = phoneNumber;
        this.district = districtCode;
        this.userType = userType;
        this.preferredLanguage = preferredLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getSubjectCodeIds() {
        return subjectCodeIds;
    }

    public void setSubjectCodeIds(String subjectCodeIds) {
        this.subjectCodeIds = subjectCodeIds;
    }

    public String getGradeCodeIds() {
        return gradeCodeIds;
    }

    public void setGradeCodeIds(String gradeCodeIds) {
        this.gradeCodeIds = gradeCodeIds;
    }

    public Integer getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(Integer preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getUdiseCode() {
        return udiseCode;
    }

    public void setUdiseCode(String udiseCode) {
        this.udiseCode = udiseCode;
    }

    public String getTopicCodeIds() {
        return topicCodeIds;
    }

    public void setTopicCodeIds(String topicCodeIds) {
        this.topicCodeIds = topicCodeIds;
    }
}
