package com.squareapps.a4teen.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.AddFriendsFragment;

public class AddFriendsActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected AddFriendsFragment createFragment() {
        return AddFriendsFragment.newInstance(getIntent().getExtras());
    }

}
