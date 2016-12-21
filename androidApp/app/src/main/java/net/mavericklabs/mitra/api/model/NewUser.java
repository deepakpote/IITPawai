package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class NewUser {
    @SerializedName("phoneNumber")
    private String mobileNumber;

    @SerializedName("authenticationType")
    private int authenticationType;

    public static int TYPE_SIGN_IN = 110101;
    public static int TYPE_REGISTER = 110100;

    public NewUser(String mobileNumber,int authenticationType) {
        this.mobileNumber = mobileNumber;
        this.authenticationType = authenticationType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
