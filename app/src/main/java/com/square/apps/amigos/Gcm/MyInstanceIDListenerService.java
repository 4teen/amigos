package com.square.apps.amigos.Gcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by YOEL on 11/3/2015.
 */
@SuppressWarnings("ALL")
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    /**
     *    * Called if InstanceID token is updated. This may occur if the security of
     *    * the previous token had been compromised. Note that this is also called
     *    * when the InstanceID token is initially generated, so this is where
     *    * you retrieve the token.
     *    
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance()
                         .subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);


    }
    // [END refresh_token]

}
