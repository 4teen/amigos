package com.squareapps.a4teen.amigos.Activities;


import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.SearchUsersFragment;


public class SearchUsersActivity extends SingleFragmentActivity {

    private SearchUsersFragment searchUsersFragment;

    @NonNull
    @Override
    protected Fragment createFragment() {
        searchUsersFragment = SearchUsersFragment.newInstance(getIntent().getStringExtra("groupID"));
        return searchUsersFragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        searchUsersFragment.handleIntent(intent);
    }




}
