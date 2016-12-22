package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class BaseModel<T> {
    @SerializedName("response_message")
    private Integer responseMessage;

    @SerializedName("data")
    private List<T> data;

    public Integer getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(Integer responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
