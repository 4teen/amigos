package com.squareapps.a4teen.amigos.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.email.EmailActivity;
import com.firebase.ui.auth.util.ui.fieldvalidators.EmailFieldValidator;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.Common.Utils.AppPreferences;
import com.squareapps.a4teen.amigos.R;

import java.util.Arrays;

import static com.squareapps.a4teen.amigos.Common.Contract.USERS;

public class LoginFragment extends FragmentBase {

    //Private statics Constants
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


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
        initGoogleSigning();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        View view = getActivity().findViewById(R.id.fragmentContainer);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                onAuthSuccess(FirebaseAuth.getInstance().getCurrentUser());
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // UserHolder pressed back button
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
                .build(), RC_SIGN_IN);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }

        
    }

    private void onAuthSuccess(final FirebaseUser user) {
        String displayName = user.getDisplayName();

        if (displayName == null)
            displayName = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(displayName, user.getEmail());
        // Go to TabhomeActivity
        getDataRef().child(USERS)
                .child(getUid())
                .child("token")
                .setValue(FirebaseInstanceId.getInstance().getToken());
    }


    // [START basic_write]
    private void writeNewUser(String displayName, String email) {
        AppPreferences.setPrefUserEmail(getContext(), email);
        AppPreferences.setPrefDisplayName(getContext(), displayName);

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();

    }
    // [END basic_write]

}




