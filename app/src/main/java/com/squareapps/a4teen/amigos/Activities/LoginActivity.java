package com.squareapps.a4teen.amigos.Activities;


import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.LoginFragment;


public class LoginActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected LoginFragment createFragment() {
        return new LoginFragment();
    }

}