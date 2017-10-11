package com.squareapps.a4teen.amigos.Activities;

import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.CollegePickerFragment;

/**
 * Created by y-pol on 10/8/2017.
 */

public class CollegePickerActivity extends SingleFragmentActivity {
    @NonNull
    @Override
    protected CollegePickerFragment createFragment() {
        return CollegePickerFragment.newInstance(null);
    }
}
