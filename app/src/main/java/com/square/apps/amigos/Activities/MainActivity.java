package com.square.apps.amigos.Activities;

import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.MainScreenFragment;



public class MainActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected MainScreenFragment createFragment() {
        return new MainScreenFragment();
    }

}


