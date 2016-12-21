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

package net.mavericklabs.mitra.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishakha on 12/12/16.
 */

public class SavedSelfLearningRequest {
    @SerializedName("userID")
    private String userID;

    @SerializedName("contentTypeCodeID")
    private Integer contentTypeCodeID;

    @SerializedName("languageCodeID")
    private String language;

    @SerializedName("topicCodeIDs")
    private String topic;



    public SavedSelfLearningRequest(String userID, Integer contentTypeCodeID, String language, String topic) {
        this.userID = userID;
        this.contentTypeCodeID = contentTypeCodeID;
        this.language = language;
        this.topic = topic;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getContentTypeCodeID() {
        return contentTypeCodeID;
    }

    public void setContentTypeCodeID(Integer contentTypeCodeID) {
        this.contentTypeCodeID = contentTypeCodeID;
    }
}
