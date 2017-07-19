package com.square.apps.amigos.Gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.square.apps.amigos.Activities.TabHome;
import com.square.apps.amigos.R;

import java.util.Map;


/**
 * Created by YOEL on 11/3/2015.
 */
@SuppressWarnings("ALL")
public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmListenerService";


    public MyFcmListenerService() {
    }

    @Override
    public void onDeletedMessages() {
        Log.i(TAG, "Received deleted messages notification");       ;
        sendNotification("gcm deleted", "GCM message", null);
    }

    @Override
    public void onMessageSent(String msgId) {
        sendNotification(msgId, "GCM message", null);
    }


    // [START receive_message]
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();

        sendNotification(message.getData().get("message"),from,null);
        handleNow();
    }



    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String from, Bitmap senderImage) {
        Intent intent = new Intent(this, TabHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setLargeIcon(senderImage)
                .setContentTitle(from)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }

    /**

     * Handle time allotted to BroadcastReceivers.

     */

    private void handleNow() {

        Log.d(TAG, "Short lived task is done.");

    }
}
