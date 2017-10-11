package com.squareapps.a4teen.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.PhoneAuthFragment;


public class PhoneAuthActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected Fragment createFragment() {
        return PhoneAuthFragment.newInstance(getIntent().getExtras());
    }
}
