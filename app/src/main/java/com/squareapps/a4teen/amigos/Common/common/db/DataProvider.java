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

import static com.squareapps.a4teen.amigos.Common.Contract.TABLE_CONTACTS;
import static java.io.File.separator;


public class DataProvider extends ContentProvider {

    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_COUNT = "count";

    public static final String AUTHORITY = "com.square.apps.provider";
    public static final String PROVIDER = "content://com.square.apps.provider";

    public static final Uri CONTENT_URI_CONTACTS = Uri.parse(PROVIDER + separator + TABLE_CONTACTS);

    private static final int CONTACTS_ALLROWS = 1;
    private static final int CONTACTS_SINGLE_ROW = 2;

    @NonNull
    private static final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_CONTACTS, CONTACTS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TABLE_CONTACTS + "/#", CONTACTS_SINGLE_ROW);
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
            case CONTACTS_ALLROWS:
                qb.setTables(getTableName(uri));
                break;

            case CONTACTS_SINGLE_ROW:
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

            case CONTACTS_ALLROWS:
                id = db.insertWithOnConflict(TABLE_CONTACTS, null, values, SQLiteDatabase.CONFLICT_FAIL);
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
            case CONTACTS_ALLROWS:
                count = db.delete(getTableName(uri), selection, selectionArgs);
                break;

            case CONTACTS_SINGLE_ROW:
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
            case CONTACTS_ALLROWS:
                count = db.update(getTableName(uri), values, selection, selectionArgs);
                break;

            case CONTACTS_SINGLE_ROW:
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
            case CONTACTS_ALLROWS:
            case CONTACTS_SINGLE_ROW:
                return Contract.TABLE_CONTACTS;
            default:
                throw new IllegalArgumentException("Table not found: " + uri);
        }

    }
}
