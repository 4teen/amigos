package com.squareapps.a4teen.amigos.Common.common.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareapps.a4teen.amigos.Common.Contract;


public class DataProvider extends ContentProvider {

    public static final String COL_ID = "_id";

    public static final String COL_MSG = "message";
    public static final String COL_FROM = "col_from";
    public static final String COL_TO = "col_to";
    public static final String COL_AT = "col_at";

    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_COUNT = "count";

    public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_MESSAGES);
    public static final Uri CONTENT_URI_PROFILE = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_PROFILE);
    public static final Uri CONTENT_URI_FRIENDS = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_FRIENDS);
    public static final Uri CONTENT_URI_ALL_FRIENDS = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_ALL_FRIENDS);
    public static final Uri CONTENT_URI_COURSES = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_COURSES);
    public static final Uri CONTENT_URI_FRIENDREQUESTS = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_FRIEND_REQUESTS);
    public static final Uri CONTENT_URI_PENDINGREQUESTS = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_PENDING_FRIENDS);
    public static final Uri CONTENT_URI_COURSES_BUFFER = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_COURSES_BUFFER);
    public static final Uri CONTENT_URI_FRIENDS_BUFFER = Uri.parse("content://com.square.apps.provider/" + Contract.TABLE_FRIENDS_BUFFER);


    private static final int MESSAGES_ALLROWS = 1;
    private static final int MESSAGES_SINGLE_ROW = 2;
    private static final int PROFILE_ALLROWS = 3;
    private static final int PROFILE_SINGLE_ROW = 4;
    private static final int FRIENDS_ALLROWS = 5;
    private static final int FRIENDS_SINGLE_ROW = 6;
    private static final int COURSES_ALLROWS = 8;
    private static final int COURSES_SINGLE_ROW = 9;
    private static final int ALL_FRIENDS_ALLROWS = 10;
    private static final int ALL_FRIENDS_SINGLE_ROW = 11;
    private static final int FRIENDREQUEST_ALLROWS = 12;
    private static final int FRIENDREQUEST_SINGLE_ROW = 13;
    private static final int PENDINGREQUEST_ALLROWS = 14;
    private static final int PENDINGREQUEST_SINGLE_ROW = 15;
    private static final int COURSE_BUFFER_ALLROWS = 16;
    private static final int COURSE_BUFFER_SINGLE_ROW = 17;
    private static final int FRIENDS_BUFFER_ALLROWS = 18;
    private static final int FRIENDS_BUFFER_SINGLE_ROW = 19;
    @NonNull
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_MESSAGES, MESSAGES_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_MESSAGES + "/#", MESSAGES_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_PROFILE, PROFILE_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_PROFILE + "/#", PROFILE_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_FRIENDS, FRIENDS_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_FRIENDS + "/#", FRIENDS_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_COURSES, COURSES_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_COURSES + "/#", COURSES_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_ALL_FRIENDS, ALL_FRIENDS_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_ALL_FRIENDS + "/#", ALL_FRIENDS_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_FRIEND_REQUESTS, FRIENDREQUEST_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_FRIEND_REQUESTS + "/#", FRIENDREQUEST_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_PENDING_FRIENDS, PENDINGREQUEST_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_PENDING_FRIENDS + "/#", PENDINGREQUEST_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_COURSES_BUFFER, COURSE_BUFFER_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_COURSES_BUFFER + "/#", COURSE_BUFFER_SINGLE_ROW);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_FRIENDS_BUFFER, FRIENDS_BUFFER_ALLROWS);
        uriMatcher.addURI("com.square.apps.provider", Contract.TABLE_FRIENDS_BUFFER + "/#", FRIENDS_BUFFER_SINGLE_ROW);
    }

    //private dataMembers
    @Nullable
    private SQLiteHandler sqLiteHandler;

    @Override
    public boolean onCreate() {
        sqLiteHandler = new SQLiteHandler(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        assert sqLiteHandler != null;
        SQLiteDatabase db = sqLiteHandler.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            case PROFILE_ALLROWS:
            case FRIENDS_ALLROWS:
            case COURSES_ALLROWS:
            case ALL_FRIENDS_ALLROWS:
            case FRIENDREQUEST_ALLROWS:
            case PENDINGREQUEST_ALLROWS:
            case COURSE_BUFFER_ALLROWS:
            case FRIENDS_BUFFER_ALLROWS:
                qb.setTables(getTableName(uri));
                break;

            case MESSAGES_SINGLE_ROW:
            case PROFILE_SINGLE_ROW:
            case FRIENDS_SINGLE_ROW:
            case ALL_FRIENDS_SINGLE_ROW:
            case COURSES_SINGLE_ROW:
            case FRIENDREQUEST_SINGLE_ROW:
            case PENDINGREQUEST_SINGLE_ROW:
            case COURSE_BUFFER_SINGLE_ROW:
            case FRIENDS_BUFFER_SINGLE_ROW:
                qb.setTables(getTableName(uri));
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues values) {
        assert sqLiteHandler != null;
        SQLiteDatabase db = sqLiteHandler.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
                id = db.insertOrThrow(Contract.TABLE_MESSAGES, null, values);
                if (values.get(COL_TO).equals("me")) {
                    db.execSQL("update all_friends set count=count+1 where email = ?", new Object[]{values.get(COL_FROM)});
                    getContext().getContentResolver().notifyChange(CONTENT_URI_ALL_FRIENDS, null);
                }
                break;

            case PROFILE_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_PROFILE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;

            case COURSES_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_COURSES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;

            case FRIENDS_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_FRIENDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;

            case ALL_FRIENDS_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_ALL_FRIENDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;

            case FRIENDREQUEST_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_FRIEND_REQUESTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;

            case PENDINGREQUEST_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_PENDING_FRIENDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case COURSE_BUFFER_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_COURSES_BUFFER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case FRIENDS_BUFFER_ALLROWS:
                id = db.insertWithOnConflict(Contract.TABLE_FRIENDS_BUFFER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null);
        return insertUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        assert sqLiteHandler != null;
        SQLiteDatabase db = sqLiteHandler.getWritableDatabase();

        int count;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            case PROFILE_ALLROWS:
            case FRIENDS_ALLROWS:
            case COURSES_ALLROWS:
            case ALL_FRIENDS_ALLROWS:
            case FRIENDREQUEST_ALLROWS:
            case PENDINGREQUEST_ALLROWS:
            case COURSE_BUFFER_ALLROWS:
            case FRIENDS_BUFFER_ALLROWS:
                count = db.delete(getTableName(uri), selection, selectionArgs);
                break;

            case MESSAGES_SINGLE_ROW:
            case PROFILE_SINGLE_ROW:
            case FRIENDS_SINGLE_ROW:
            case ALL_FRIENDS_SINGLE_ROW:
            case COURSES_SINGLE_ROW:
            case FRIENDREQUEST_SINGLE_ROW:
            case PENDINGREQUEST_SINGLE_ROW:
            case COURSE_BUFFER_SINGLE_ROW:
            case FRIENDS_BUFFER_SINGLE_ROW:
                count = db.delete(getTableName(uri), "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        assert sqLiteHandler != null;
        SQLiteDatabase db = sqLiteHandler.getWritableDatabase();

        int count;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            case PROFILE_ALLROWS:
            case FRIENDS_ALLROWS:
            case ALL_FRIENDS_ALLROWS:
            case COURSES_ALLROWS:
            case FRIENDREQUEST_ALLROWS:
            case PENDINGREQUEST_ALLROWS:
            case COURSE_BUFFER_ALLROWS:
            case FRIENDS_BUFFER_ALLROWS:
                count = db.update(getTableName(uri), values, selection, selectionArgs);
                break;

            case MESSAGES_SINGLE_ROW:
            case PROFILE_SINGLE_ROW:
            case FRIENDS_SINGLE_ROW:
            case ALL_FRIENDS_SINGLE_ROW:
            case COURSES_SINGLE_ROW:
            case FRIENDREQUEST_SINGLE_ROW:
            case PENDINGREQUEST_SINGLE_ROW:
            case COURSE_BUFFER_SINGLE_ROW:
            case FRIENDS_BUFFER_SINGLE_ROW:
                count = db.update(getTableName(uri), values, "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String getTableName(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            case MESSAGES_SINGLE_ROW:
                return Contract.TABLE_MESSAGES;

            case PROFILE_ALLROWS:
            case PROFILE_SINGLE_ROW:
                return Contract.TABLE_PROFILE;

            case FRIENDS_ALLROWS:
            case FRIENDS_SINGLE_ROW:
                return Contract.TABLE_FRIENDS;

            case ALL_FRIENDS_ALLROWS:
            case ALL_FRIENDS_SINGLE_ROW:
                return Contract.TABLE_ALL_FRIENDS;

            case COURSES_ALLROWS:
            case COURSES_SINGLE_ROW:
                return Contract.TABLE_COURSES;

            case FRIENDREQUEST_ALLROWS:
            case FRIENDREQUEST_SINGLE_ROW:
                return Contract.TABLE_FRIEND_REQUESTS;

            case PENDINGREQUEST_ALLROWS:
            case PENDINGREQUEST_SINGLE_ROW:
                return Contract.TABLE_PENDING_FRIENDS;

            case COURSE_BUFFER_ALLROWS:
            case COURSE_BUFFER_SINGLE_ROW:
                return Contract.TABLE_COURSES_BUFFER;

            case FRIENDS_BUFFER_ALLROWS:
            case FRIENDS_BUFFER_SINGLE_ROW:
                return Contract.TABLE_FRIENDS_BUFFER;

        }
        return null;
    }
}
