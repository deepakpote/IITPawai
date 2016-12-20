package net.mavericklabs.mitra.database.model;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/8/16.
 */

public class DbNotification extends RealmObject {
    private String title;
    private String body;
    private String type;
    private long receivedTime;

    public DbNotification() {

    }

    public DbNotification(String title, String type, String body, long time) {
        this.title = title;
        this.type = type;
        this.body = body;
        this.receivedTime = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }
}
