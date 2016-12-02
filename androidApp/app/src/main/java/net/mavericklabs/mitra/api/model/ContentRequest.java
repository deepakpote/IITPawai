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

import net.mavericklabs.mitra.utils.Constants;

import java.io.Serializable;

/**
 * Created by vishakha on 10/11/16.
 */

public class ContentRequest implements Serializable{

    @SerializedName("userID")
    private String userID;

    @SerializedName("fileTypeCodeID")
    private String fileType;

    @SerializedName("languageCodeID")
    private String language;

    @SerializedName("subjectCodeID")
    private String subject;

    @SerializedName("gradeCodeID")
    private String grade;

    @SerializedName("pageNumber")
    private int pageNumber;

    public ContentRequest(String userID, String fileType, String language, String subject, String grade) {
        this.userID = userID;
        this.fileType = fileType;
        this.language = language;
        this.subject = subject;
        this.grade = grade;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
