package com.square.apps.amigos.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.square.apps.amigos.R;
import com.squareup.picasso.Picasso;


public class MainScreenFragment extends Fragment {

    private static final int REQUEST_INVITE = 1;
    // [END declare_auth
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    @Nullable
    private Callbacks callbacks;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        /*
      private variables
     */
        ImageView myProfilePic = (ImageView) view.findViewById(R.id.quickContactBadge);
        TextView myName = (TextView) view.findViewById(R.id.profile_text_View);


        FirebaseUser user = mAuth.getCurrentUser();

        Picasso.with(getActivity()).load(user.getPhotoUrl()).placeholder(R.drawable.tampa_skyline).into(myProfilePic);

        myName.setText(user.getDisplayName());

        return view;
    }


    public interface Callbacks {
        void onTakePicture();
    }

}
