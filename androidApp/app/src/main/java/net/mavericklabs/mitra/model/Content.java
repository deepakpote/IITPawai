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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by vishakha on 10/11/16.
 */

@RealmClass
public class Content implements Serializable, RealmModel, Parcelable {

    @SerializedName("contentID")
    private String contentID;

    @SerializedName("contentTitle")
    private String title;

    @SerializedName("contentType")
    private Integer contentTypeCodeID;

    @SerializedName("subject")
    private Integer subject;

    @SerializedName("gradeCodeIDs")
    private String grade;

    @SerializedName("topic")
    private Integer topic;

    @SerializedName("requirementCodeIDs")
    private String requirement;

    @SerializedName("instruction")
    private String instruction;

    @SerializedName("fileType")
    private Integer fileType;

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("author")
    private String author;

    @SerializedName("objectives")
    private String objectives;

    @SerializedName("language")
    private Integer language;

    public Content() {

    }

    public Content(String contentID, String title, Integer contentTypeCodeID, Integer subject, String grade, String requirement, String instruction, Integer fileType, String fileName, String author, String objectives, Integer language) {
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

    protected Content(Parcel in) {
        contentID = (String) in.readValue(String.class.getClassLoader());
        title = (String) in.readValue(String.class.getClassLoader());
        grade = (String) in.readValue(String.class.getClassLoader());
        requirement = (String) in.readValue(String.class.getClassLoader());
        instruction = (String) in.readValue(String.class.getClassLoader());
        fileName = (String) in.readValue(String.class.getClassLoader());
        author = (String) in.readValue(String.class.getClassLoader());
        objectives = (String) in.readValue(String.class.getClassLoader());

        contentTypeCodeID = (Integer) in.readValue(Integer.class.getClassLoader());
        fileType = (Integer) in.readValue(Integer.class.getClassLoader());
        language = (Integer) in.readValue(Integer.class.getClassLoader());
        subject = (Integer) in.readValue(Integer.class.getClassLoader());
        topic = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

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

    public Integer getContentTypeCodeID() {
        return contentTypeCodeID;
    }

    public void setContentTypeCodeID(Integer contentTypeCodeID) {
        this.contentTypeCodeID = contentTypeCodeID;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
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

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getTopic() {
        return topic;
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(contentID);
        parcel.writeValue(title);
        parcel.writeValue(grade);
        parcel.writeValue(requirement);
        parcel.writeValue(instruction);
        parcel.writeValue(fileName);
        parcel.writeValue(author);
        parcel.writeValue(objectives);

        parcel.writeValue(contentTypeCodeID);
        parcel.writeValue(fileType);
        parcel.writeValue(language);
        parcel.writeValue(subject);
        parcel.writeValue(topic);

    }
}
