package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18/11/16.
 */

public class RegisterUser {

    @SerializedName("name")
    private String name;

    @SerializedName("otp")
    private String otp;

    @SerializedName("phone_number")
    private String phoneNumber;

    public RegisterUser(String name, String otp, String phoneNumber) {
        this.name = name;
        this.otp = otp;
        this.phoneNumber = phoneNumber;
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
