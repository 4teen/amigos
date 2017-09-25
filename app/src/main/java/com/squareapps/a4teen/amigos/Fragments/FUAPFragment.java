package com.squareapps.a4teen.amigos.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareapps.a4teen.amigos.R;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view. (Forgot User and Password)
 */
public class FUAPFragment extends Fragment {

    private static final String TAG = "FUAPFragment";

    //views
    @BindView(R.id.reset_email_email)
    EditText email;

    @BindView(R.id.forgot_password_submit_button)
    Button submit;

    Snackbar mySnackbar;

    public FUAPFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuap, container, false);
        ButterKnife.bind(this, view);


        //[START initialize_SnackBar]
        mySnackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction("Email sent", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySnackbar.dismiss();
            }
        });
        //[END initialization_SnackBar]

        email.setError(null);

        //[START register_clickListener]
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = email.getText().toString();
                if (!TextUtils.isEmpty(newEmail) && newEmail.contains("@")) {
                    sendEmailVerification(newEmail);
                } else {
                    email.setError("invalid e-mail");
                    email.requestFocus();
                }
            }
        });
        //[END register_clickListener]

        return view;
    }

    /**
     * sends verification to email
     *
     * @param newEmail is a valid email address
     */
    private void sendEmailVerification(@NonNull String newEmail) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            mySnackbar.show();
                        }
                    }
                });
    }
}
