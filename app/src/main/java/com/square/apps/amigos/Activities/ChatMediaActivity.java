package com.square.apps.amigos.Activities;

import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.ChatMediaFragment;

public class ChatMediaActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected ChatMediaFragment createFragment() {
        String groupId = getIntent().getStringExtra(ChatMediaFragment.PARAM1);
        return ChatMediaFragment.newInstance(groupId);
    }

}
