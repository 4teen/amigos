package com.squareapps.a4teen.amigos.Abstract;

import android.app.Fragment;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareapps.a4teen.amigos.Fragments.LoginFragment;
import com.squareapps.a4teen.amigos.R;

import static com.squareapps.a4teen.amigos.Common.Contract.AVATAR_URL;


public abstract class FragmentBase extends Fragment {

    private View myView;


    public ActionBar setToolbar(Toolbar toolbar, int res) {
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

        return supportActionBar;
    }


    @Nullable
    public String getAvatarUrl() {
        return PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(AVATAR_URL, null);
    }

    public boolean setAvatarUrl(String avatarUrl) {
        if (avatarUrl == null) return false;
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit().putString(AVATAR_URL, avatarUrl)
                .apply();
        return true;
    }


    public static DatabaseReference getDataRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public static String getUid() {
        return getUser().getUid();
    }

    @NonNull
    protected static Boolean isSignedIn() {
        return (getUser() != null);
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

    public void setImageView(String url, ImageView imageView) {
        if (url == null) {
            return;
        }

        url = Uri.decode(url);

        if (url.startsWith("gs")) {
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(url);

            // Load the image using Glide
            GlideApp.with(getContext())
                    .load(storageReference)
                    .fitCenter()
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .into(imageView);
        }
    }

    public View getMyView() {
        return myView;
    }

    public void showSnackbar(String comment) {
        Snackbar.make(myView.findViewById(android.R.id.content), comment,
                Snackbar.LENGTH_SHORT).show();
    }

    public View newItemView(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);
    }

    public void setMyView(View myView) {
        this.myView = myView;
    }

}
