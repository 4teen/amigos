package com.squareapps.a4teen.amigos.Common.common.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import static com.squareapps.a4teen.amigos.Common.Contract.TABLE_CONTACTS;
import static com.squareapps.a4teen.amigos.Common.common.db.DataProvider.COL_COUNT;
import static com.squareapps.a4teen.amigos.Common.common.db.DataProvider.COL_ID;
import static com.squareapps.a4teen.amigos.Common.common.db.DataProvider.COL_NAME;
import static com.squareapps.a4teen.amigos.Common.common.db.DataProvider.COL_PHONE;


class SQLiteHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "databaseAmigos";


    SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // create table contacts
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " VARCHAR(50),"
                + COL_PHONE + " TEXT,"
                + COL_COUNT + " INTEGER DEFAULT 0)";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /**
     * Upgrading database
     * Drop older tables if existed
     * Create tables again
     **/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}