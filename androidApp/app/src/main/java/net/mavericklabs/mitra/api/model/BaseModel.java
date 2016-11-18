package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 18/11/16.
 */

public class BaseModel<T> {
    @SerializedName("response_message")
    private String responseMessage;

    @SerializedName("data")
    private List<T> data;
}
