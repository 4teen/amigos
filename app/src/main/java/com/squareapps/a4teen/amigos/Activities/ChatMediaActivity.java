package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.ChatMediaFragment;


public class ChatMediaActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected ChatMediaFragment createFragment() {
        String groupId = getIntent().getStringExtra(ChatMediaFragment.PARAM1);
        return ChatMediaFragment.newInstance(groupId);
    }

}
