package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amoghpalnitkar on 12/7/16.
 */

public class LikeRequest {
    @SerializedName("userID")
    private String userId;

    @SerializedName("contentID")
    private String contentId;

    @SerializedName("hasLiked")
    private boolean hasLiked;

    public LikeRequest(String userId, String contentId, boolean hasLiked) {
        this.userId = userId;
        this.contentId = contentId;
        this.hasLiked = hasLiked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }
}
