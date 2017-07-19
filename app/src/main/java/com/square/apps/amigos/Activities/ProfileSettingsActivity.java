package com.square.apps.amigos.Activities;

import android.app.Fragment;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.ProfileSettingsFragment;

/**
 * Created by y-pol on 7/11/2017.
 */

public class ProfileSettingsActivity extends SingleFragmentActivity implements ProfileSettingsFragment.OnFragmentInteractionListener {
    @NonNull
    @Override
    protected Fragment createFragment() {
        return ProfileSettingsFragment.newInstance(null, null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
