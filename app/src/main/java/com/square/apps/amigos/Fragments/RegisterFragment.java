package com.square.apps.amigos.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.square.apps.amigos.Activities.TabHome;
import com.square.apps.amigos.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class RegisterFragment extends Fragment {

    // declare static constants
    private static final String TAG = "CreateAccount";

    // declare buttons
    private Button btnRegister;

    //declare views
    private EditText fullName;
    private EditText email;
    private EditText password;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //[START declare_progressBar]
    private ProgressBar progresBar;
    //[END declare progressBar]

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);

        // views
        fullName = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        //[START initialize_progressBar]
        progresBar = (ProgressBar) view.findViewById(R.id.fragment_register_progress_bar);
        progresBar.setMax(100);
        //[END initialize_progressBar]

        //button listeners initialization
        button_register_listener();
        return view;
    }

    /**
     * Register Button Click event
     */
    private void button_register_listener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String newName = fullName.getText().toString();
                String newEmail = email.getText().toString();
                String newPassword = password.getText().toString();
                createAccount(newName, newEmail, newPassword);

            }
        });
    }

    /**
     * function creates account with email and password
     *
     * @param email
     * @param password
     */
    private void createAccount(final String name, @NonNull String email, @NonNull String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        progresBar.setVisibility(VISIBLE);

        // [START create_user_with_email]

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            //[START updating user profile]
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                initiateTabHomeActivity();
                                            } else {
                                                Log.d(TAG, "Error updating user.");
                                            }
                                        }
                                    });
                            //[END updating user profile]


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Exception e = task.getException();
                            Log.d("exception", e.getMessage());
                        }
                        progresBar.setVisibility(GONE);
                    }
                });


        // [END create_user_with_email]
    }

    /**
     * @return true when email and password are valid and false otherwise
     */
    private boolean validateForm() {
        // Reset errors.
        email.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String newEmail = email.getText().toString();
        String newPassword = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(newPassword) && !isPasswordValid(newPassword)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        } else if (TextUtils.isEmpty(newPassword)) {
            password.setError("invalid password");
            focusView = password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(newEmail)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(newEmail)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            return true;
        }
    }

    /**
     * @param email
     * @return true if email is valid
     */
    private boolean isEmailValid(@NonNull String email) {
        return email.contains("@");
    }

    /**
     * @param password
     * @return true if password is valid
     */
    private boolean isPasswordValid(@NonNull String password) {
        return password.length() > 4;
    }

    /**
     * function passes control to TabHome.class
     */
    private void initiateTabHomeActivity() {
        Intent i = new Intent(getActivity(), TabHome.class);
        startActivity(i);
        getActivity().finish();
    }


}
