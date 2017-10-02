package com.squareapps.a4teen.amigos.Abstract;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareapps.a4teen.amigos.R;

/**
 * Created by y-pol on 9/25/2017.
 */

public abstract class BaseTaskService extends IntentService {

    static final int PROGRESS_NOTIFICATION_ID = 0;
    static final int FINISHED_NOTIFICATION_ID = 1;

    private static final String TAG = "MyBaseTaskService";
    private int mNumTasks = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseTaskService(String name) {
        super(name);
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }


    /**
     * Show notification with a progress bar.
     */
    protected void showProgressNotification(String caption, long completedUnits, long totalUnits) {
        int percentComplete = 0;
        if (totalUnits > 0) {
            percentComplete = (int) (100 * completedUnits / totalUnits);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_account_key_black_24dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(caption)
                .setProgress(100, percentComplete, false)
                .setOngoing(true)
                .setAutoCancel(false);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(PROGRESS_NOTIFICATION_ID, builder.build());
    }

    /**
     * Show notification that the activity finished.
     */
    protected void showFinishedNotification(String caption, Intent intent, boolean success) {
        // Make PendingIntent for notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int icon = success ? R.drawable.ic_audiotrack_dark : R.drawable.ic_facebook_white_22dp;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(caption)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(FINISHED_NOTIFICATION_ID, builder.build());
    }

    /**
     * Dismiss the progress notification.
     */
    protected void dismissProgressNotification() {
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(PROGRESS_NOTIFICATION_ID);

    }
}
