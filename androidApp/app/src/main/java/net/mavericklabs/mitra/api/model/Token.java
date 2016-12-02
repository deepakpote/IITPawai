package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 12/2/16.
 */

public class Token {
    @SerializedName("token")
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
