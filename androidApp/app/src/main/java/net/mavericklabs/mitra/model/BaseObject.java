package net.mavericklabs.mitra.model;

import java.io.Serializable;

/**
 * Created by amoghpalnitkar on 12/3/16.
 */

public class BaseObject implements Serializable{
    private CommonCode commonCode;
    private boolean isChecked;

    public BaseObject(CommonCode commonCode, boolean isChecked) {
        this.commonCode = commonCode;
        this.isChecked = isChecked;
    }

    public CommonCode getCommonCode() {
        return commonCode;
    }

    public void setCommonCode(CommonCode commonCode) {
        this.commonCode = commonCode;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
