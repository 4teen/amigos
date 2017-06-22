package com.square.apps.amigos.Activities;



import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.RegisterFragment;



public class RegisterActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected RegisterFragment createFragment() {
        return new RegisterFragment();
    }
}




