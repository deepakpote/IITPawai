package net.mavericklabs.mitra.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.mavericklabs.mitra.utils.Logger;

/**
 * Created by amoghpalnitkar on 12/7/16.
 */

public class MitraFirebaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
