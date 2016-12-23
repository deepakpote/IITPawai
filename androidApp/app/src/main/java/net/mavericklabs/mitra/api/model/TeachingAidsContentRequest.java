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

import java.io.Serializable;

/**
 * Created by vishakha on 10/11/16.
 */

public class TeachingAidsContentRequest implements Serializable{

    @SerializedName("fileTypeCodeID")
    private Integer fileType;

    @SerializedName("subjectCodeIDs")
    private String subject;

    @SerializedName("gradeCodeIDs")
    private String grade;

    @SerializedName("pageNumber")
    private int pageNumber;

    public TeachingAidsContentRequest(int fileType, String subject, String grade) {
        this.fileType = fileType;
        this.subject = subject;
        this.grade = grade;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
