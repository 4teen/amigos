package com.squareapps.a4teen.amigos.Services.Fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.R;

import java.util.Map;


/**
 * Created by YOEL on 11/3/2015.
 */
@SuppressWarnings("ALL")
public class MyFcmListenerService extends FirebaseMessagingService {

    private static int NOTIFICATION_ID = 100;

    private static final String TAG = "MyFcmListenerService";

    private NotificationTarget notificationTarget;

    public MyFcmListenerService() {
    }

    @Override
    public void onDeletedMessages() {
        Log.i(TAG, "Received deleted messages notification");
        //sendNotification("gcm deleted", "GCM message", null, 00000);
    }

    @Override
    public void onMessageSent(String msgId) {
        //sendNotification(msgId, "GCM message", null, 00000);
    }


    // [START receive_message]
    @Override
    public void onMessageReceived(@NonNull final RemoteMessage message) {
        final String from = message.getFrom();
        final Map data = message.getData();
        final RemoteMessage.Notification MessageNotification = message.getNotification();

        String tag = MessageNotification.getTag();
        //START_EXEPTION
        if (tag.equals("groupAdd")) {
            Log.d("notification", "tag");
        }

        sendNotification(MessageNotification, from, message.getSentTime());
    }


    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notification GCM message received.
     */
    private void sendNotification(RemoteMessage.Notification notification, String from, long timeStamp) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        String ringtoneValue = preferences
                .getString(getString(R.string.notifications_new_message_ringtone), null);

        boolean vibrate = preferences
                .getBoolean(getString(R.string.notifications_new_message_vibrate), false);
        Uri defaultSoundUri = Uri.parse(ringtoneValue);

        long[] pattern;
        if (vibrate)
            pattern = new long[]{0, 400, 1000};
        else
            pattern = new long[]{0};

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setColor(795548)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setWhen(timeStamp)
                .setVibrate(pattern)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        Notification notif = notificationBuilder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notif);


    }

    /**
     * Handle time allotted to BroadcastReceivers.
     *
     * @param notification
     * @param remoteViews
     * @param firebaseNotif
     */

    private void handleNow(final Notification notification, RemoteViews remoteViews, RemoteMessage.Notification firebaseNotif) {


    }

}
