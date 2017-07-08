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

package net.mavericklabs.mitra.model.database;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/8/16.
 */

public class DbNotification extends RealmObject {
    private String title;
    private String body;
    private String type;
    private String notificationTypeID;
    private String objectID;
    private long receivedTime;

    public DbNotification() {

    }

    public DbNotification(String title, String body, String type, String notificationTypeID, String objectID, long receivedTime) {
        this.title = title;
        this.body = body;
        this.type = type;
        this.notificationTypeID = notificationTypeID;
        this.objectID = objectID;
        this.receivedTime = receivedTime;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotificationTypeId() {
        return notificationTypeID;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeID = notificationTypeId;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
