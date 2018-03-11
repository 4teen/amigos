package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.InvitationFragment;

/**
 * Created by y-pol on 2/27/2018.s
 */

public class InvitationDetailActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected InvitationFragment createFragment() {
        return InvitationFragment.newInstance(getIntent().getExtras());
    }
}
