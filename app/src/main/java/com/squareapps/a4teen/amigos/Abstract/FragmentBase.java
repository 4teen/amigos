package com.squareapps.a4teen.amigos.Abstract;

import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Fragments.LoginFragment;

import static com.squareapps.a4teen.amigos.Common.Contract.AVATAR_URL;

/**
 * Created by y-pol on 9/24/2017.
 */

public abstract class FragmentBase extends Fragment {

    @Nullable
    public String getAvatarUrl() {
        return PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(AVATAR_URL, null);
    }

    public DatabaseReference getDataRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getUid() {
        return getUser().getUid();
    }

    @MainThread
    public void showSnackbar(@StringRes int errorMessageRes, View view) {
        Snackbar.make(view, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    public void signOutUser() {
        AuthUI.getInstance()
                .signOut((AppCompatActivity) getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginFragment.createIntent(getContext()));
                            (getActivity()).finish();
                        }
                    }
                });
    }


}
