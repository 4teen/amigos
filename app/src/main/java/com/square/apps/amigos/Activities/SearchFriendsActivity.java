package com.square.apps.amigos.Activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.SearchFriendsFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.AddDeleteFriendsService;
import com.square.apps.amigos.common.common.db.DataProvider;


public class SearchFriendsActivity extends SingleFragmentActivity implements SearchFriendsFragment.Callbacks {

    @NonNull
    @Override
    protected SearchFriendsFragment createFragment() {
        return new SearchFriendsFragment();
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        SearchFriendsFragment searchFriendsFragment = (SearchFriendsFragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        searchFriendsFragment.handleIntent(intent);
    }

    @Override
    public void onSelectedFriend(String StudentID) {
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_FRIENDS_BUFFER, StudentID), null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Contract.NAME));
        cursor.close();

        final Intent intent = new Intent(this, AddDeleteFriendsService.class);
        intent.putExtra(Contract.STUDENT_ID, StudentID);
        /*
          displays an alert dialog double checking that the user wants to add the course
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ADD " + name + " to your friends?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("tag", "sendFriendRequest");
                        startService(intent);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //intent.putExtra(acceptRejectFriendRequest.TAG, "rejectFriendRequest");
                        // startService(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
