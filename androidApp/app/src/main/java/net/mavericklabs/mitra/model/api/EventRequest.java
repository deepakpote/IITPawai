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

package net.mavericklabs.mitra.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishakha on 09/12/16.
 */

public class EventRequest {

    @SerializedName("timeMin")
    private String timeMin;

    @SerializedName("timeMax")
    private String timeMax;

    @SerializedName("orderBy")
    private String orderBy;

    @SerializedName("singleEvents")
    private String singleEvents;

    @SerializedName("maxResults")
    private String maxResults;

    public EventRequest(String timeMin, String timeMax, String orderBy) {
        this.timeMin = timeMin;
        this.timeMax = timeMax;
        this.orderBy = orderBy;
        this.singleEvents = "true";
        this.maxResults = "100";
    }

    public String getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(String timeMin) {
        this.timeMin = timeMin;
    }

    public String getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(String timeMax) {
        this.timeMax = timeMax;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSingleEvents() {
        return singleEvents;
    }

    public void setSingleEvents(String singleEvents) {
        this.singleEvents = singleEvents;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }
}
