package com.square.apps.amigos.common.common.Friend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.common.common.db.DataProvider;

import java.util.ArrayList;


public class FriendLab {

    @SuppressLint("StaticFieldLeak")
    private static FriendLab friendLab;
    private final Context context;

    private FriendLab(Context mAppContext) {
        ArrayList<Friend> tempFriends = new ArrayList<>();
        context = mAppContext;
    }

    public static FriendLab get(@NonNull Context context) {
        if (friendLab == null) {
            friendLab = new FriendLab(context.getApplicationContext());
        }
        return friendLab;
    }

    @NonNull
    public ArrayList<Friend> getFriends() {
        Cursor cursor = context.getContentResolver().query(DataProvider.CONTENT_URI_ALL_FRIENDS, null, null, null, null);
        return toArray(cursor);
    }

    @Nullable
    public Friend getFriend(String searchValue, String SearchKey) {
        Cursor cursor = context.getContentResolver().query(DataProvider.CONTENT_URI_ALL_FRIENDS, null, SearchKey + " = ?", new String[]{searchValue}, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return toFriendObject(cursor);
        } else
            return null;
    }

    @NonNull
    private ArrayList<Friend> toArray(@NonNull Cursor cursor) {
        ArrayList<Friend> friends = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                friends.add(toFriendObject(cursor));
            }
        }
        return friends;
    }

    @NonNull
    private Friend toFriendObject(@NonNull Cursor cursor) {
        Friend friend = new Friend();
        friend.setFriendID(cursor.getString(cursor.getColumnIndex(Contract.PRIMARY_KEY)));
        friend.setEmail(cursor.getString(cursor.getColumnIndex(Contract.EMAIL)));
        friend.setName(cursor.getString(cursor.getColumnIndex(Contract.NAME)));
        friend.setProfilePic();
        friend.setCount(cursor.getString(cursor.getColumnIndex(Contract.COUNT)));
        Log.d("myFriend", friend.toString());
        return friend;
    }



}
