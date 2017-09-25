package com.squareapps.a4teen.amigos.Activities;



import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.RegisterFragment;


public class RegisterActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected RegisterFragment createFragment() {
        return new RegisterFragment();
    }
}




