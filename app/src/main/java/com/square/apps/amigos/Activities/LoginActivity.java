package com.square.apps.amigos.Activities;


import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.LoginFragment;

public class LoginActivity extends SingleFragmentActivity{
    @NonNull
    @Override
    protected LoginFragment createFragment() {
        return new LoginFragment();
    }

}