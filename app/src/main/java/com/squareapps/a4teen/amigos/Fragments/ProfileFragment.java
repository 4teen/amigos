package com.squareapps.a4teen.amigos.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Common.common.FirebaseUtils;
import com.squareapps.a4teen.amigos.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.squareapps.a4teen.amigos.Common.Contract.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;


public class ProfileFragment extends FragmentBase {
    public static final String PHOTO_URL = "photoUrl";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ProfileFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE = 0400;
    private final FirebaseUtils firebaseUtils = new FirebaseUtils() {
    };
    @BindView(R.id.profile_image_circleImageView)
    ImageView imageViewCircle;
    @BindView(R.id.profile_name_editText)
    EditText name;
    @BindView(R.id.profile_birthdate_text)
    EditText birtdate;
    @BindView(R.id.profile_cellphone_text)
    EditText phoneNumber;
    @BindView(R.id.profile_fab)
    FloatingActionButton fab;
    private ValueEventListener valueEventListener;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseUser user;
    private DatabaseReference myref;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        myref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        ButterKnife.bind(this, view);

        name.setText(user.getDisplayName());
        phoneNumber.setText(user.getPhoneNumber());
        firebaseUtils.setImageView(getAvatarUrl(), imageViewCircle);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Select image for image message on click.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    firebaseUtils.setImageView(uri, imageViewCircle);

                    StorageReference storageReference = FirebaseStorage
                            .getInstance()
                            .getReference(user.getUid())
                            .child(PHOTO_URL)
                            .child(uri.getLastPathSegment());

                    putImageInStorage(storageReference, uri);

                }
                break;

        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri) {
        storageReference
                .putFile(uri)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String photoUrl = task.getResult().getMetadata()
                                    .getDownloadUrl()
                                    .toString();

                            HashMap<String, Object> map = new HashMap<>();
                            map.put(USERS + separator
                                    + user.getUid() + separator
                                    + AVATAR_URL, photoUrl);

                            myref.updateChildren(map);

                        } else {
                            Log.w(TAG, "Image upload task was not successful.", task.getException());
                        }
                    }
                });

    }

}
