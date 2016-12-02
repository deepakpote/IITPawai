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

import net.mavericklabs.mitra.utils.Constants;

import java.io.Serializable;

/**
 * Created by vishakha on 10/11/16.
 */

public class Content implements Serializable{
    private String title;
    private Constants.FileType fileType;
    private Constants.Type type;

    public Content(String title, Constants.FileType fileType, Constants.Type type) {
        this.title = title;
        this.fileType = fileType;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Constants.FileType getFileType() {
        return fileType;
    }

    public void setFileType(Constants.FileType fileType) {
        this.fileType = fileType;
    }

    public Constants.Type getType() {
        return type;
    }

    public void setType(Constants.Type type) {
        this.type = type;
    }
}
