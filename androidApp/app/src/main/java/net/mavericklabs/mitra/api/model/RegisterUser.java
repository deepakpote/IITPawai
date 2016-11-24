package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18/11/16.
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

    public RegisterUser(String userName, String otp, String phoneNumber, String districtCode) {
        this.name = userName;
        this.otp = otp;
        this.phoneNumber = phoneNumber;
        this.district = districtCode;
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
}
