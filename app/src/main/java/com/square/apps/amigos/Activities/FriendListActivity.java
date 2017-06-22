package com.square.apps.amigos.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.FriendListFragment;
import com.square.apps.amigos.R;


public class FriendListActivity extends SingleFragmentActivity implements FriendListFragment.Callbacks {
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_masterdetail;
    }

    @NonNull
    @Override
    protected FriendListFragment createFragment() {
        String courseID = getIntent().getStringExtra(Contract.COURSE_ID);
        return  FriendListFragment.newInstance(courseID);
    }

    @Override
    public void onFriendSelected(String friendID) {
        Intent friendIntent = new Intent(this, FriendActivity.class);
        friendIntent.putExtra(Contract.PRIMARY_KEY, friendID);
        startActivity(friendIntent);
    }
}
