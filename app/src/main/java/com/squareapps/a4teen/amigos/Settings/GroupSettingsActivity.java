package com.squareapps.a4teen.amigos.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Abstract.ChangeGroupAvatarPreference;
import com.squareapps.a4teen.amigos.Abstract.GroupSettingsCallback;
import com.squareapps.a4teen.amigos.Common.POJOS.Photo;
import com.squareapps.a4teen.amigos.Common.Utils.AppPreferences;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.Services.UploadService;

import java.util.HashMap;

import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.MEDIA;
import static com.squareapps.a4teen.amigos.Common.Contract.PATH;
import static com.squareapps.a4teen.amigos.Common.Contract.User.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.User.PHOTO_URL;
import static com.squareapps.a4teen.amigos.Services.UploadService.EXTRA_FILE_URI;
import static java.io.File.separator;

/**
 * Created by y-pol on 1/16/2018.s
 */

public class GroupSettingsActivity extends AppCompatActivity implements
        GroupSettingsCallback {

    public static final int REQUEST_IMAGE_GET = 1;
    private String groupId, avatarUrl;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public void onStart() {
        super.onStart();
        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, UploadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister download receiver
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            avatarUrl = getIntent().getStringExtra(AVATAR_URL);
            groupId = getIntent().getStringExtra(GROUP_ID);
        } else {
            avatarUrl = savedInstanceState.getString(AVATAR_URL);
            groupId = savedInstanceState.getString(GROUP_ID);
        }

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //    hideProgressDialog();

                switch (intent.getAction()) {
                    case UploadService.UPLOAD_COMPLETED:
                    case UploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }
            }
        };

        SettingsActivity.GeneralPreferenceFragment settingsFragment = new SettingsActivity.GeneralPreferenceFragment();
        settingsFragment.setArguments(getIntent().getExtras());

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GROUP_ID, groupId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getAvatarUrl() {
        return AppPreferences.getPrefGroupAvatarUrl(this, groupId);
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            final Uri fullPhotoUri = data.getData();

            final String myUid = FirebaseAuth.getInstance().getUid();
            final DatabaseReference dataRef = FirebaseDatabase.getInstance()
                    .getReference(MEDIA + separator + groupId);

            final String photoID = dataRef.push().getKey();

            Photo photo = new Photo(null, myUid, groupId, photoID);

            dataRef.setValue(photo, (databaseError, databaseReference) -> {
                String update1 = MEDIA + separator +
                        groupId + separator +
                        photoID + separator +
                        PHOTO_URL;

                uploadImage(fullPhotoUri, photoID, update1);

            });

        } else {
            Log.d("SettingsActivity", String.valueOf(requestCode));

        }
    }

    private void uploadImage(Uri data, String photoID, String... strings) {

        Bundle bundle = new Bundle();
        bundle.putStringArray("updates", strings);
        bundle.putString(PATH, MEDIA + separator + groupId + photoID);
        bundle.putParcelable(EXTRA_FILE_URI, data);
        UploadService.startActionUpload(this, bundle);
    }


    private void onUploadResultIntent(Intent intent) {

        // Got a new intent from MyUploadService with a success or failure
        String mDownloadUrl = intent.getStringExtra(UploadService.EXTRA_DOWNLOAD_URL);
        Log.d("downloadUrl", mDownloadUrl);

        String[] updates = intent.getStringArrayExtra("updates");

        HashMap<String, Object> map = new HashMap<>();
        for (String update : updates) {
            map.put(update, mDownloadUrl);
            Log.d("updates", update);
        }
        FirebaseDatabase.getInstance().getReference()
                .updateChildren(map);
        AppPreferences.setPrefGroupAvatarUrl(this, groupId, mDownloadUrl);

        SettingsActivity.GeneralPreferenceFragment fragment = (SettingsActivity.GeneralPreferenceFragment) getFragmentManager()
                .findFragmentById(android.R.id.content);

        ((ChangeGroupAvatarPreference) fragment.findPreference(getString(R.string.key_group_avatar)))
                .setPhotoUrl(mDownloadUrl);

    }
}



