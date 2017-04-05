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

import net.mavericklabs.mitra.utils.Logger;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by vishakha on 24/01/17.
 */

public class Migration implements RealmMigration {

    //Hacky crash fix - http://stackoverflow.com/a/36919305

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Migration);
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        Logger.d("Old  " + oldVersion);
        if(oldVersion == 0) {
            Logger.d(" oldversion " + oldVersion);

            // Create a new class
            schema.create("News")
                    .addField("newsID", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("newsTitle", String.class)
                    .addField("author", String.class)
                    .addField("imageURL", String.class)
                    .addField("content", String.class)
                    .addField("createdOn", String.class)
                    .addField("publishDate", String.class)
                    .addField("department", Integer.class)
                    .addField("dateToCompare", Date.class)
                    .addField("modifiedOn", String.class)
                    .addField("pdfFileURL", String.class)
                    .addField("isSeen", boolean.class)
                    .addField("isSaved", boolean.class)
                    .addField("showOnMainPage", boolean.class);

            schema.create("Language")
                    .addField("language", Integer.class);
            oldVersion++;
        }

        if(oldVersion == 1) {
            schema.create("Content")
                    .addField("contentID", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("title", String.class)
                    .addField("contentTypeCodeID",Integer.class)
                    .addField("subject", Integer.class)
                    .addField("grade", String.class)
                    .addField("topic", Integer.class)
                    .addField("requirement",String.class)
                    .addField("instruction",String.class)
                    .addField("fileType", Integer.class)
                    .addField("fileName",String.class)
                    .addField("author",String.class)
                    .addField("objectives",String.class)
                    .addField("language",Integer.class);
        }

    }
}
