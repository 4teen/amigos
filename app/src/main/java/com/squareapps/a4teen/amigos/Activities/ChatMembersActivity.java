package com.squareapps.a4teen.amigos.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.ChatMembersFragment;


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

        return ChatMembersFragment.newInstance(bundle);
    }

    private void setAction(Bundle bundle) {
        bundle.putString(ACTION, getIntent().getAction());

    }

}
