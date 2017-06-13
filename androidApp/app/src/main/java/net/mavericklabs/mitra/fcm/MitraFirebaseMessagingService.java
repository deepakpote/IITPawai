package net.mavericklabs.mitra.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.database.Migration;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.utils.MitraContextWrapper;
import net.mavericklabs.mitra.utils.StringUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by amoghpalnitkar on 12/7/16.
 */

public class MitraFirebaseMessagingService extends FirebaseMessagingService{

    //Add more notification types here
    public final String NOTIFICATION_TYPE_DEFAULT = "0";

    @Override
    protected void attachBaseContext(Context newBase) {

        //Set realm schema version
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(3) // Must be bumped when the schema changes
                .migration(new Migration()) // Migration to run
                .build();

        Realm.setDefaultConfiguration(config);


        Context context = MitraContextWrapper.wrap(newBase, LanguageUtils.getCurrentLocale());
        super.attachBaseContext(context);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Logger.d("message received : " + remoteMessage.getData());
        Logger.d("message received : " + remoteMessage.getData().get("body"));

        String message = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        String type = remoteMessage.getData().get("type");
        if(StringUtils.isEmpty(type)) {
            type = NOTIFICATION_TYPE_DEFAULT;
        }

        DbNotification notification = new DbNotification(title,
                                                type,
                                                remoteMessage.getData().get("body"),
                                                System.currentTimeMillis());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insert(notification);
        realm.commitTransaction();

        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("show_notification",true);
        intent.putExtras(bundle);
        intent.setAction(Long.toString(System.currentTimeMillis()));

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_icon_black_white_small)
                        .setLargeIcon(notificationLargeIconBitmap)
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0))
                        .setContentText(message);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
