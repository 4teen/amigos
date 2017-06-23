package com.square.apps.amigos.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.square.apps.amigos.Activities.LoginActivity;
import com.square.apps.amigos.R;
import com.squareup.picasso.Picasso;


public class MainScreenFragment extends Fragment {

    private Button logout;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth

    @Nullable
    private Callbacks callbacks;

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        /*
      private variables
     */
        ImageView myProfilePic = (ImageView) view.findViewById(R.id.quickContactBadge);
        TextView myName = (TextView) view.findViewById(R.id.profile_text_View);

        /* Initializing buttons**/
        logout = (Button) view.findViewById(R.id.button_logout);


        FirebaseUser user = mAuth.getCurrentUser();

        Picasso.with(getActivity()).load(user.getPhotoUrl()).placeholder(R.drawable.tampa_skyline).into(myProfilePic);

        myName.setText(user.getDisplayName());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.profile_image_fab);

        /* Starting Listeners**/

        Button_logout_Listener();
        return view;
    }

    private void Button_logout_Listener(){
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                logoutUser();
            }
        });
    }

    private void logoutUser(){
        mAuth.signOut();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    public interface Callbacks {
        void onTakePicture();
    }

}  /*
  end of class
 */

