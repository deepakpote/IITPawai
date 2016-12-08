package net.mavericklabs.mitra.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.mavericklabs.mitra.database.model.DbNotification;
import net.mavericklabs.mitra.utils.Logger;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amoghpalnitkar on 12/7/16.
 */

public class MitraFirebaseMessagingService extends FirebaseMessagingService{

    //Add more notification types here
    public final String NOTIFICATION_TYPE_DEFAULT = "0";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Logger.d("message received : " + remoteMessage.getNotification().getBody());
        DbNotification notification = new DbNotification(remoteMessage.getNotification().getTitle(),
                                                NOTIFICATION_TYPE_DEFAULT,
                                                remoteMessage.getNotification().getBody());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insert(notification);
        realm.commitTransaction();
    }
}
