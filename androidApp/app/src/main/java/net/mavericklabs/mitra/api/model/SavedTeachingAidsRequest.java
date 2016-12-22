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

public class SavedTeachingAidsRequest {
    @SerializedName("userID")
    private String userID;

    @SerializedName("contentTypeCodeID")
    private Integer contentTypeCodeID;

    @SerializedName("fileTypeCodeIDs")
    private String fileType;

    @SerializedName("subjectCodeIDs")
    private String subject;

    @SerializedName("gradeCodeIDs")
    private String grade;


    public SavedTeachingAidsRequest(String userID, Integer contentTypeCodeID, String fileType, String subject, String grade) {
        this.userID = userID;
        this.contentTypeCodeID = contentTypeCodeID;
        this.fileType = fileType;
        this.subject = subject;
        this.grade = grade;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
