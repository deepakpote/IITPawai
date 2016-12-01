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

package net.mavericklabs.mitra.model;

import com.google.gson.annotations.SerializedName;

import net.mavericklabs.mitra.utils.Constants;

import java.io.Serializable;

/**
 * Created by vishakha on 10/11/16.
 */

public class Content implements Serializable{

    @SerializedName("contentID")
    private String contentID;

    @SerializedName("contentTitle")
    private String title;

    @SerializedName("contentType")
    private String contentTypeCodeID;

    @SerializedName("subject")
    private String subject;

    @SerializedName("grade")
    private String grade;

    @SerializedName("requirement")
    private String requirement;

    @SerializedName("instruction")
    private String instruction;

    @SerializedName("fileType")
    private String fileType;

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("author")
    private int author;

    @SerializedName("objectives")
    private String objectives;

    @SerializedName("language")
    private String language;

    public Content(String contentID, String title, String contentTypeCodeID, String subject, String grade, String requirement, String instruction, String fileType, String fileName, int author, String objectives, String language) {
        this.contentID = contentID;
        this.title = title;
        this.contentTypeCodeID = contentTypeCodeID;
        this.subject = subject;
        this.grade = grade;
        this.requirement = requirement;
        this.instruction = instruction;
        this.fileType = fileType;
        this.fileName = fileName;
        this.author = author;
        this.objectives = objectives;
        this.language = language;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentTypeCodeID() {
        return contentTypeCodeID;
    }

    public void setContentTypeCodeID(String contentTypeCodeID) {
        this.contentTypeCodeID = contentTypeCodeID;
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

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
