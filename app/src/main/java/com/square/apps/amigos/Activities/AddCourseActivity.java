package com.square.apps.amigos.Activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.AddCourseFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.AddCourseService;
import com.square.apps.amigos.common.common.db.DataProvider;

public class AddCourseActivity extends SingleFragmentActivity implements AddCourseFragment.Callbacks {

    private static ProgressDialog progressDialog;
    @Nullable
    private BroadcastReceiver searchCourseBroadcastReceiver;

    @NonNull
    @Override
    protected AddCourseFragment createFragment() {
        return new AddCourseFragment();
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        AddCourseFragment addCourseFragment = (AddCourseFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        addCourseFragment.handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(searchCourseBroadcastReceiver,
                new IntentFilter(Contract.SEARCH_COURSES_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(searchCourseBroadcastReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.setCancelable(false);

        searchCourseBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                progressDialog.dismiss();
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) { //auto-hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        };
    }


    @Override
    public void onSelectedCourse(String CourseID) {
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_COURSES_BUFFER, CourseID), null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(Contract.TITLE));

        final Intent intent = new Intent(this, AddCourseService.class);
        intent.putExtra(Contract.COURSE_ID, CourseID);
        /*
          displays an alert dialog double checking that the user wants to add the course
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startService(intent);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //intent.putExtra(acceptRejectFriendRequest.TAG, "rejectFriendRequest");
                        // startService(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        cursor.close();
    }
}
