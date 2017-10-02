package com.squareapps.a4teen.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.ProfileFragment;


/**
 * Created by y-pol on 7/11/2017.
 */

public class ProfileActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected Fragment createFragment() {
        return ProfileFragment.newInstance(null);
    }

}
