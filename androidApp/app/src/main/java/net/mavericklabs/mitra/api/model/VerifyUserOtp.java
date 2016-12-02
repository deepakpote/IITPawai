package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class VerifyUserOtp {

    @SerializedName("phoneNumber")
    private String mobileNumber;

    @SerializedName("otp")
    private String otp;

    @SerializedName("authenticationType")
    private String authenticationType;

    public VerifyUserOtp(String mobileNumber, String otp, String authenticationType) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
        this.authenticationType = authenticationType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
