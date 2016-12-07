package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private String district;

    @SerializedName("userType")
    private String userType;

    @SerializedName("subjectCodeIDs")
    private String subjectCodeIds;

    @SerializedName("gradeCodeIDs")
    private String gradeCodeIds;

    @SerializedName("preferredLanguage")
    private String preferredLanguage;

    public RegisterUser(String userName, String otp, String phoneNumber, String districtCode, String userType, String preferredLanguage) {
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
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

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}
