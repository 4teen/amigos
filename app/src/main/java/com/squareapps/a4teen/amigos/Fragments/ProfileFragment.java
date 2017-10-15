package com.squareapps.a4teen.amigos.Fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.CollegePickerActivity;
import com.squareapps.a4teen.amigos.Activities.PhoneAuthActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Photo;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.UploadService;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.squareapps.a4teen.amigos.Common.Contract.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.BIRTHDATE;
import static com.squareapps.a4teen.amigos.Common.Contract.COLLEGE;
import static com.squareapps.a4teen.amigos.Common.Contract.GENDER;
import static com.squareapps.a4teen.amigos.Common.Contract.NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.PATH;
import static com.squareapps.a4teen.amigos.Common.Contract.PHONE_NUMBER;
import static com.squareapps.a4teen.amigos.Common.Contract.PHOTO_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;


public class ProfileFragment extends FragmentBase implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 401;
    private static final int REQUEST_IMAGE = 400;
    private static final int REQUEST_CELLPHONE = 402;
    private static final int REQUEST_COLLEGE = 403;


    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
    public static final String AUTHORITY = "com.squareapps.a4teen.amigos.fileprovider";

    private BroadcastReceiver mBroadcastReceiver;

    private ValueEventListener valueEventListener;
    private DatabaseReference myref;
    private String avatarUrl;
    private DatePickerDialog datePickerDialog;

    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;
    private File mPhotoFile;

    @BindView(R.id.profile_image_circleImageView)
    ImageView imageViewCircle;

    @BindView(R.id.profile_fab)
    FloatingActionButton fab;

    @BindView(R.id.profile_name_text_view)
    TextView name;

    @BindView(R.id.profile_birthday_text_view)
    TextView birtdate;

    @BindView(R.id.profile_phone_text_view)
    TextView cellphone;

    @BindView(R.id.profile_school_text_view)
    TextView school;

    @BindView(R.id.profile_gender_text_view)
    TextView gender;


    public static ProfileFragment newInstance(Bundle bundle) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(mBroadcastReceiver, UploadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister download receiver
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myref.removeEventListener(valueEventListener);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_FILE_URI, mFileUri);
        outState.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore instance state
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }


        // Local broadcast receiver
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

        myref = getDataRef();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_detail, container, false);
        ButterKnife.bind(this, view);

        valueEventListener = myref.child(USERS)
                .child(getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            switch (snapshot.getKey()) {
                                case AVATAR_URL:
                                    avatarUrl = snapshot.getValue().toString();
                                    setImageView(avatarUrl, imageViewCircle);
                                    setAvatarUrl(avatarUrl);
                                    break;
                                case NAME:
                                    name.setText(snapshot.getValue().toString());
                                    break;
                                case COLLEGE:
                                    school.setText(snapshot.getValue().toString());
                                    break;
                                case GENDER:
                                    gender.setText(snapshot.getValue().toString());
                                    break;
                                case BIRTHDATE:
                                    String dateString = snapshot.getValue().toString();
                                    birtdate.setText(dateString);
                                    break;
                                case PHONE_NUMBER:
                                    cellphone.setText(snapshot.getValue().toString());
                                    break;

                            }

                        }
                        if (name.getText().length() <= 0) {
                            name.setText(getUser().getDisplayName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        setBirthdate();
        fab.setOnClickListener(this);
        cellphone.setOnClickListener(this);
        school.setOnClickListener(this);
        registerForContextMenu(fab);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_birthday_text_view:
                datePickerDialog.show();
                break;
            case R.id.profile_fab:
                v.showContextMenu();
                break;
            case R.id.profile_phone_text_view:
                Bundle bundle = new Bundle();
                bundle.putString(PHONE_NUMBER, cellphone.getText().toString());

                Intent phoneAuth = new Intent(getActivity(), PhoneAuthActivity.class);
                phoneAuth.putExtras(bundle);

                getActivity().startActivityForResult(phoneAuth, REQUEST_CELLPHONE);
                break;
            case R.id.profile_school_text_view:
                Intent collegePicker = new Intent(getActivity(), CollegePickerActivity.class);
                startActivityForResult(collegePicker, REQUEST_COLLEGE);
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:

                Uri uriFile = FileProvider.getUriForFile(getActivity(),
                        AUTHORITY,
                        mPhotoFile);

                getActivity().revokeUriPermission(uriFile,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                uploadImage(uriFile);
            case REQUEST_IMAGE:
                if (data != null) {
                    final Uri uri = data.getData();
                    uploadImage(uri);
                }
                break;

            case REQUEST_COLLEGE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String college = bundle.getString(COLLEGE);
                    getDataRef().child(USERS)
                            .child(getUid())
                            .child(COLLEGE)
                            .setValue(college);
                }

                break;
        }

    }

    private void setBirthdate() {
        birtdate.setOnClickListener(this);
        final Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                getDataRef()
                        .child(USERS)
                        .child(getUid())
                        .child(BIRTHDATE).setValue(newDate.get(Calendar.YEAR) + " "
                        + newDate.get(Calendar.MONTH) + " "
                        + newDate.get(Calendar.DAY_OF_MONTH));

            }

        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Profile Photo");
        menu.add(0, 100, 0, "From File");
        menu.add(0, 101, 0, "From Camera");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 100:
                dispatchPictureIntent();
                break;
            case 101:
                dispatchTakePictureIntent();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void dispatchPictureIntent() {
        // Select image for image message on click.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void dispatchTakePictureIntent() {
        Photo photo = new Photo();
        File filesDir = getContext().getFilesDir();
        mPhotoFile = new File(filesDir, photo.getPhotoFilename());

        Uri uri = FileProvider.getUriForFile(getActivity(),
                AUTHORITY,
                mPhotoFile);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> cameraActivities = getActivity()
                .getPackageManager().queryIntentActivities(takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        if (mPhotoFile != null && takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private void uploadImage(Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putString(PATH, getUid() + separator + PHOTO_URL);
        bundle.putParcelable(UploadService.EXTRA_FILE_URI, uri);
        UploadService.startActionUpload(getContext(), bundle);
    }


    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        String mDownloadUrl = intent.getStringExtra(UploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(UploadService.EXTRA_FILE_URI);

        Log.d("downloadUrl", mDownloadUrl);

        HashMap<String, Object> map = new HashMap<>();
        map.put(USERS + separator
                + getUid() + separator
                + AVATAR_URL, mDownloadUrl);

        myref.updateChildren(map);

    }


}
