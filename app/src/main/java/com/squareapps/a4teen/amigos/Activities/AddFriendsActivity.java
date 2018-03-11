package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.AddContactsFragment;

public class AddFriendsActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected AddContactsFragment createFragment() {
        return AddContactsFragment.newInstance(getIntent().getExtras());
    }

}
