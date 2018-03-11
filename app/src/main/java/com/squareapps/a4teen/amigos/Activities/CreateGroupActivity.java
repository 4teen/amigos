package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.CreateGroupFragment;

/**
 * Created by y-pol on 1/14/2018.s
 */

public class CreateGroupActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected CreateGroupFragment createFragment() {
        return CreateGroupFragment.newInstance(null);
    }
}
