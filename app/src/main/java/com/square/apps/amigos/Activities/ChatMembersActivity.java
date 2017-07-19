package com.square.apps.amigos.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.ChatMembersFragment;

public class ChatMembersActivity extends SingleFragmentActivity {

    public final static String ACTION = "Action";

    @NonNull
    @Override
    protected ChatMembersFragment createFragment() {
        String groupId = getIntent().getStringExtra(ChatMembersFragment.EXTRA_PARAM1);
        Bundle bundle = new Bundle();
        if (getIntent().getAction() != null)
            setAction(bundle);
        bundle.putString(ChatMembersFragment.EXTRA_PARAM1, groupId);

        ChatMembersFragment fragment = ChatMembersFragment.newInstance(bundle);
        return fragment;
    }

    private void setAction(Bundle bundle) {
        bundle.putString(ACTION, getIntent().getAction());

    }

}
