package com.squareapps.a4teen.amigos.Abstract;

import android.app.Fragment;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.R;

import static com.squareapps.a4teen.amigos.Common.Contract.User.AVATAR_URL;


public abstract class FragmentBase extends Fragment {

    private View myView;

    protected static DatabaseReference getDataRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getSimpleName(Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    private FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    protected void setToolbar(Toolbar toolbar, int res) {
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), res, getActivity().getTheme());
            assert indicator != null;
            indicator
                    .setTint(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Nullable
    protected String getAvatarUrl() {
        return PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(AVATAR_URL, null);
    }

    protected void setAvatarUrl(String avatarUrl) {
        if (avatarUrl == null) return;
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit().putString(AVATAR_URL, avatarUrl)
                .apply();
    }

    protected FirebaseUser getUser() {
        return getAuth().getCurrentUser();
    }

    protected String getUid() {
        return getUser().getUid();
    }

    protected Boolean isSignedIn() {
        return (getUser() != null);
    }

    protected void signOutUser() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        (getActivity()).finish();
                    }
                });
    }

    @MainThread
    protected void showSnackbar(@StringRes int errorMessageRes, View view) {
        Snackbar.make(view, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    protected void showSnackbar(String comment) {
        Snackbar.make(myView.findViewById(android.R.id.content), comment,
                Snackbar.LENGTH_SHORT).show();
    }

    protected View newItemView(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);
    }

    protected void setMyView(View myView) {
        this.myView = myView;
    }

}
