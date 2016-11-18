package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18/11/16.
 */

public class RegisterUserResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("token")
    private String token;

    public RegisterUserResponse(String name, String token) {
        this.name = name;
        this.token = token;
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
}
