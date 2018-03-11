package com.squareapps.a4teen.amigos.Services;

import android.content.Context;
import android.content.Intent;

import com.squareapps.a4teen.amigos.Abstract.BaseTaskService;
import com.squareapps.a4teen.amigos.Common.Utils.FirebaseFunctions;

public class NotificationService extends BaseTaskService {
    private static final String TAG = "NotificationService";
    private static final String ACTION_GROUP_ADD = "com.squareapps.a4teen.amigos.action.GROUP_ADD";

    private static final String FROM = "com.squareapps.a4teen.amigos.extra.FROM";
    private static final String TO = "com.squareapps.a4teen.amigos.extra.TO";
    private static final String GROUP_ID = "com.squareapps.a4teen.amigos.extra.GROUP_ID";

    public NotificationService() {
        super(TAG);
    }

    public static void startActionGroupAdd(Context context, String from, String to, String groupId) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_GROUP_ADD);
        intent.putExtra(FROM, from);
        intent.putExtra(TO, to);
        intent.putExtra(GROUP_ID, groupId);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GROUP_ADD.equals(action)) {
                final String from = intent.getStringExtra(FROM);
                final String to = intent.getStringExtra(TO);
                final String groupId = intent.getStringExtra(GROUP_ID);
                handleActionGroupAdd(from, to, groupId);

            }
        }
    }

    /**
     * Handle action Group Add in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGroupAdd(String from, String to, String groupId) {
        FirebaseFunctions firebaseFunctions = new FirebaseFunctions();
        firebaseFunctions.sendGroupAddNotification(from, to, groupId);
    }

}
