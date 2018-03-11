package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.NotificationsFragment;

public class NotificationsActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected NotificationsFragment createFragment() {
        return NotificationsFragment.newInstance(getIntent().getExtras());
    }
}
