package com.square.apps.amigos.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.AppControl;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.FriendFragment;
import com.square.apps.amigos.Services.AddDeleteFriendsService;
import com.square.apps.amigos.common.common.db.DataProvider;


public class FriendActivity extends SingleFragmentActivity implements FriendFragment.Callbacks {
    private String friendID;

    @NonNull
    @Override
    protected FriendFragment createFragment() {
        friendID = getIntent().getSerializableExtra(Contract.PRIMARY_KEY).toString();
        return FriendFragment.newInstance(friendID);
    }

    @Override
    public void onChatWithFriend() {
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra(Contract.PRIMARY_KEY, friendID);
        startActivity(chatIntent);
    }

    @Override
    public void onDeleteFriend() {

        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_ALL_FRIENDS, friendID), null, null, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Contract.NAME));
        cursor.close();

        final Intent intent = new Intent(this, AddDeleteFriendsService.class);
        intent.putExtra(Contract.STUDENT_ID, friendID);
        /*
          displays an alert dialog double checking that the user wants to remove amigo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Remove " + name + " From your Amigos?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("tag", AppControl.DELETEFRIEND_TAG);
                        startService(intent);
                        getContentResolver().delete(DataProvider.CONTENT_URI_ALL_FRIENDS, null, null);
                        getContentResolver().delete(DataProvider.CONTENT_URI_FRIENDS, null,null);
                        Intent goHomeIntent = new Intent(getApplicationContext(), TabHome.class);
                        startActivity(goHomeIntent);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
