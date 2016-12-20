package net.mavericklabs.mitra.database.model;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/11/16.
 */

public class DbTopic extends RealmObject{
    private String topicCommonCode;

    public DbTopic() {

    }

    public DbTopic(String topicCommonCode) {
        this.topicCommonCode = topicCommonCode;
    }

    public String getTopicCommonCode() {
        return topicCommonCode;
    }

    public void setTopicCommonCode(String topicCommonCode) {
        this.topicCommonCode = topicCommonCode;
    }
}
