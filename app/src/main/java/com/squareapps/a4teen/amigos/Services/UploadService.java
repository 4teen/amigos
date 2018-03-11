package com.squareapps.a4teen.amigos.Services;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareapps.a4teen.amigos.Abstract.BaseTaskService;
import com.squareapps.a4teen.amigos.Common.Utils.FirebaseContactsLoader;

import java.util.Objects;

import static com.squareapps.a4teen.amigos.Common.Contract.PATH;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;


public class UploadService extends BaseTaskService {

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String ACTION_UPLOAD_CONTACTS = "action_upload_contacts";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    /**
     * Intent Extras
     **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    private static final String TAG = "MyUploadService";


    public UploadService() {
        super(TAG);
    }


    public static void startActionUpload(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_UPLOAD);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startActionUploadContacts(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_UPLOAD_CONTACTS);
        //intent.putExtras(bundle);
        context.startService(intent);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (ACTION_UPLOAD.equals(action)) {
                handleActionUpload(intent);
            } else if (ACTION_UPLOAD_CONTACTS.equals(action)) {
                handleActionUploadContacts(intent);
            }
        }
    }

    private void handleActionUpload(Intent intent) {
        Uri fileUri = intent.getExtras().getParcelable(EXTRA_FILE_URI);
        String path = intent.getExtras().getString(PATH);
        uploadFromUri(fileUri, path, intent);
    }

    private void handleActionUploadContacts(Intent intent) {
        final FirebaseContactsLoader contacstLoader = new FirebaseContactsLoader(getApplicationContext());
        final Integer count = contacstLoader.getContactsCursor().getCount();

        FirebaseDatabase.getInstance().getReference(USERS + separator + getUid() + separator + "contactsCount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!Objects.equals(dataSnapshot.getValue(), count)) {
                            contacstLoader.uploadNumbers();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void uploadFromUri(final Uri fileUri, final String path, final Intent intent) {

        final StorageReference photoRef = getStorageRef().child(path)
                .child(fileUri.getLastPathSegment());

        photoRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            // Get the public download URL
            String downloadUrl = taskSnapshot.getStorage().toString();
            broadcastUploadFinished(downloadUrl, fileUri, intent);
        })
                .addOnFailureListener(exception -> broadcastUploadFinished(null, fileUri, intent));
    }

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

}
