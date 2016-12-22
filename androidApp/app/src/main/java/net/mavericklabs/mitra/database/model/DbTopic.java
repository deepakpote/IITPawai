package net.mavericklabs.mitra.database.model;

import io.realm.RealmObject;

/**
 * Created by amoghpalnitkar on 12/11/16.
 */

public class DbTopic extends RealmObject{
    private Integer topicCommonCode;

    public DbTopic() {

    }

    public DbTopic(Integer topicCommonCode) {
        this.topicCommonCode = topicCommonCode;
    }

    public Integer getTopicCommonCode() {
        return topicCommonCode;
    }

    public void setTopicCommonCode(Integer topicCommonCode) {
        this.topicCommonCode = topicCommonCode;
    }
}
