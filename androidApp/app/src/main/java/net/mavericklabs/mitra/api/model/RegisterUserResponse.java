package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class RegisterUserResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("token")
    private String token;

    @SerializedName("userID")
    private String userID;

    public RegisterUserResponse(String name, String token, String userID) {
        this.name = name;
        this.token = token;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
