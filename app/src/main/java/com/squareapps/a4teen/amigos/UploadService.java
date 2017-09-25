package com.squareapps.a4teen.amigos;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;


public class UploadService extends IntentService {

    private static final String ACTION_FOO = "com.squareapps.a4teen.amigos.action.FOO";
    private static final String ACTION_BAZ = "com.squareapps.a4teen.amigos.action.BAZ";


    private static final String EXTRA_PARAM1 = "com.squareapps.a4teen.amigos.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.squareapps.a4teen.amigos.extra.PARAM2";

    public UploadService() {
        super("UploadService");
    }


    public static void startActionUpload(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }


    private void handleActionFoo(String param1, String param2) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void handleActionBaz(String param1, String param2) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
