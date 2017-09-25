package com.squareapps.a4teen.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.ChatFragment;
import com.squareapps.a4teen.amigos.Fragments.GroupListFragment;


public class ChatActivity extends SingleFragmentActivity {


    @NonNull
    @Override
    protected Fragment createFragment() {
        return ChatFragment.newInstance(getIntent().getBundleExtra(GroupListFragment.ARGS));
    }


}
