package com.square.apps.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.ChatFragment;
import com.square.apps.amigos.Fragments.GroupListFragment;


public class ChatActivity extends SingleFragmentActivity {


    @NonNull
    @Override
    protected Fragment createFragment() {
        return ChatFragment.newInstance(getIntent().getBundleExtra(GroupListFragment.ARGS));
    }


}
