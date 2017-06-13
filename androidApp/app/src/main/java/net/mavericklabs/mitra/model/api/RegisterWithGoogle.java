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
 * Created by vishakha on 22/05/17.
 */

public class RegisterWithGoogle {
    @SerializedName("googleToken")
    private String idToken;

    @SerializedName("fcmDeviceID")
    private String fcmToken;

    @SerializedName("fcmRegistrationRequired")
    private String fcmRegistrationRequired;

    public RegisterWithGoogle(String idToken, String fcmToken, String fcmRegistrationRequired) {
        this.idToken = idToken;
        this.fcmToken = fcmToken;
        this.fcmRegistrationRequired = fcmRegistrationRequired;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmRegistrationRequired() {
        return fcmRegistrationRequired;
    }

    public void setFcmRegistrationRequired(String fcmRegistrationRequired) {
        this.fcmRegistrationRequired = fcmRegistrationRequired;
    }
}
