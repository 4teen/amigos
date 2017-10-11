package com.squareapps.a4teen.amigos;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareapps.a4teen.amigos.Abstract.BaseTaskService;

import static com.squareapps.a4teen.amigos.Common.Contract.PATH;


public class UploadService extends BaseTaskService {

    private static final String TAG = "MyUploadService";

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /**
     * Intent Extras
     **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";


    public UploadService() {
        super(TAG);
    }


    public static void startActionUpload(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_UPLOAD);
        intent.putExtras(bundle);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (ACTION_UPLOAD.equals(action)) {
                handleActionUpload(intent);
            }
        }
    }


    private void handleActionUpload(Intent intent) {
        Uri fileUri = intent.getExtras().getParcelable(EXTRA_FILE_URI);
        String path = intent.getExtras().getString(PATH);
        uploadFromUri(fileUri, path, intent);
    }

    private void uploadFromUri(final Uri fileUri, final String path, final Intent intent) {

        showProgressNotification(getString(R.string.app_name), 0, 0);

        final StorageReference photoRef = getStorageRef().child(path)
                .child(fileUri.getLastPathSegment());

        photoRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(getString(R.string.send),
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the public download URL
                        String downloadUrl = taskSnapshot.getStorage().toString();
                        broadcastUploadFinished(downloadUrl, fileUri, intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        broadcastUploadFinished(null, fileUri, intent);

                    }
                });
    }
    // [END upload_from_uri]

    /**
     * Broadcast finished upload (success or failure).
     *
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(String downloadUrl, @Nullable Uri fileUri, Intent i) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtras(i.getExtras())
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }


    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }

}
