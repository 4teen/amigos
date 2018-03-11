package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.InvitationFragment;
import com.squareapps.a4teen.amigos.Fragments.InvitationListFragment;


public class InvitationActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected InvitationListFragment createFragment() {
        return InvitationListFragment.newInstance(getIntent().getExtras());
    }
}
