package com.squareapps.a4teen.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.FUAPFragment;


public class FUAPActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected Fragment createFragment() {
        return new FUAPFragment();
    }


}