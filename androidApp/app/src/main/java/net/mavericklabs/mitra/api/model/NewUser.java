package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class NewUser {
    @SerializedName("phoneNumber")
    private String mobileNumber;

    @SerializedName("authenticationType")
    private String authenticationType;

    public static String TYPE_SIGN_IN = "110101";
    public static String TYPE_REGISTER = "110100";

    public NewUser(String mobileNumber,String authenticationType) {
        this.mobileNumber = mobileNumber;
        this.authenticationType = authenticationType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }
}
