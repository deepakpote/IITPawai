package net.mavericklabs.mitra.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amoghpalnitkar on 12/22/16.
 */

public class CommonCodeWrapper {
    @SerializedName("version")
    private String version;

    @SerializedName("codeList")
    private List<CommonCode> commonCode;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<CommonCode> getCommonCode() {
        return commonCode;
    }

    public void setCommonCode(List<CommonCode> commonCode) {
        this.commonCode = commonCode;
    }
}
