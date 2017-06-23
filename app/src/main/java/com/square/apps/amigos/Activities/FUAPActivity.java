package com.square.apps.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.FUAPFragment;

public class FUAPActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected Fragment createFragment() {
        return new FUAPFragment();
    }


}
