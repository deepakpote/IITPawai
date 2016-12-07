package net.mavericklabs.mitra.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by amoghpalnitkar on 12/7/16.
 */

public class MitraFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //TODO implement token refresh on server
    }
}
