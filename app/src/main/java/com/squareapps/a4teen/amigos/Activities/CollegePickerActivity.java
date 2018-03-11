package com.squareapps.a4teen.amigos.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.CollegePickerFragment;


public class CollegePickerActivity extends SingleFragmentActivity {
    private CollegePickerFragment collegePickerFragment;

    @NonNull
    @Override
    protected CollegePickerFragment createFragment() {
        collegePickerFragment = CollegePickerFragment.newInstance(null);
        return collegePickerFragment;
    }

   /* When the system calls onNewIntent(Intent), the activity has not been restarted, so the getIntent()
    method returns the same intent that was received with onCreate().
    This is why you should call setIntent(Intent) inside onNewIntent(Intent)
    (so that the intent saved by the activity is updated in case you call getIntent() in the future).*/
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        collegePickerFragment.handleIntent(intent);

    }
}
