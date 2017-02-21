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

public class VerifyUserOtp {

    @SerializedName("phoneNumber")
    private String mobileNumber;

    @SerializedName("otp")
    private String otp;

    @SerializedName("authenticationType")
    private int authenticationType;

    @SerializedName("fcmDeviceID")
    private String fcmToken;

    @SerializedName("fcmRegistrationRequired")
    private String fcmRegistrationRequired;

    public VerifyUserOtp(String mobileNumber, String otp, int authenticationType, String fcmToken,
                         String fcmRegistrationRequired) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
        this.authenticationType = authenticationType;
        this.fcmToken = fcmToken;
        this.fcmRegistrationRequired = fcmRegistrationRequired;
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
