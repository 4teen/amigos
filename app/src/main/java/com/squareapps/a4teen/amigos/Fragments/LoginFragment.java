package com.squareapps.a4teen.amigos.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.R;

import java.util.Arrays;
import java.util.HashMap;

import static com.squareapps.a4teen.amigos.Common.Contract.EMAIL;
import static com.squareapps.a4teen.amigos.Common.Contract.NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;

/**
 * Created by YOEL on 10/22/2015.
 */


public class LoginFragment extends FragmentBase {

    //Private statics Constants
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "EmailPassword";

    View view;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private DatabaseReference mDatabase;

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, LoginActivity.class);
        return in;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            onAuthSuccess(currentUser);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initGoogleSigning();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                onAuthSuccess(FirebaseAuth.getInstance().getCurrentUser());
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled, view);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection, view);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error, view);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response, view);
        }
    }


    private void initGoogleSigning() {
        startActivityForResult(AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER)
                        .build(), new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER)
                        .build(), new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                        .build()))
                .setIsSmartLockEnabled(false)
                .setAllowNewEmailAccounts(true)
                .setTheme(R.style.AppTheme_Base)
                .build(), RC_SIGN_IN);
    }


    private void initiateTabHomeActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();

    }

    private void onAuthSuccess(final FirebaseUser user) {
        String username = user.getDisplayName();

        if (username == null)
            username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        // Go to TabhomeActivity
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        HashMap<String, Object> mymap = new HashMap<>();
        mymap.put(USERS + separator + userId + separator + NAME, name);
        mymap.put(USERS + separator + userId + separator + EMAIL, email);
        mDatabase.updateChildren(mymap);
        initiateTabHomeActivity();
    }

    // [END basic_write]

}




