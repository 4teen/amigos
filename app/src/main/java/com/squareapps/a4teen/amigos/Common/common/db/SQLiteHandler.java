package com.squareapps.a4teen.amigos.Common.common.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Common.Contract;


class SQLiteHandler extends SQLiteOpenHelper {

    //TODO: delete amigosDataBase

    public SQLiteHandler(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // create table login
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + Contract.TABLE_PROFILE + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.NAME + " VARCHAR(50),"
                + Contract.PROFILE_IMAGE + " LONGBLOB DEFAULT NULL,"
                + Contract.EMAIL + " VARCHAR(100) UNIQUE,"
                + Contract.UID + " VARCHAR(23),"
                + Contract.CREATED_AT + " DATETIME" + ")";

        //create table friends
        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + Contract.TABLE_FRIENDS + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Contract.EMAIL + " TEXT,"
                + Contract.NAME + " TEXT UNIQUE,"
                + Contract.COURSE_ID + " INTEGER" + ")";

        //create table friends
        String CREATE_ALL_FRIENDS_TABLE = "CREATE TABLE " + Contract.TABLE_ALL_FRIENDS + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.EMAIL + " TEXT,"
                + Contract.NAME + " TEXT UNIQUE,"
                + Contract.PROFILE_IMAGE + " LONGBLOB DEFAULT NULL,"
                + Contract.COUNT + " INTEGER DEFAULT 0)";

        //create table friends
        String CREATE_FRIENDS_BUFFER_TABLE = "CREATE TABLE " + Contract.TABLE_FRIENDS_BUFFER + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.EMAIL + " TEXT,"
                + Contract.NAME + " TEXT UNIQUE" + ")";

        //create table courses
        String CREATE_COURSE_TABLE = "CREATE TABLE " + Contract.TABLE_COURSES + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.COLLEGE + " TEXT,"
                + Contract.DEPARTMENT + " TEXT,"
                + Contract.CRN + " TEXT UNIQUE,"
                + Contract.SUBJECT_CRS + " TEXT,"
                + Contract.SECTION + " INTEGER,"
                + Contract.TITLE + " TEXT,"
                + Contract.BUILDING + " TEXT,"
                + Contract.ROOM + " TEXT,"
                + Contract.INSTRUCTOR + " TEXT,"
                + Contract.CAMPUS + " TEXT,"
                + Contract.TIME + " TEXT,"
                + Contract.DAYS + " TEXT" + ")";

        //create table courses
        String CREATE_BUFFER_COURSE_TABLE = "CREATE TABLE " + Contract.TABLE_COURSES_BUFFER + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.COLLEGE + " TEXT,"
                + Contract.DEPARTMENT + " TEXT,"
                + Contract.CRN + " TEXT UNIQUE,"
                + Contract.SUBJECT_CRS + " TEXT,"
                + Contract.SECTION + " INTEGER,"
                + Contract.TITLE + " TEXT,"
                + Contract.BUILDING + " TEXT,"
                + Contract.ROOM + " TEXT,"
                + Contract.INSTRUCTOR + " TEXT,"
                + Contract.CAMPUS + " TEXT,"
                + Contract.TIME + " TEXT,"
                + Contract.DAYS + " TEXT" + ")";

        //create table messages
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + Contract.TABLE_MESSAGES + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Contract.MESSAGE + " TEXT,"
                + Contract.FROM + " TEXT,"
                + Contract.TO + " TEXT,"
                + Contract.AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

        String CREATE_FRIEND_REQUEST_TABLE = "CREATE TABLE " + Contract.TABLE_FRIEND_REQUESTS + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.NAME + " TEXT" + ")";

        String CREATE_PENDING_FRIENDS_TABLE = "CREATE TABLE " + Contract.TABLE_PENDING_FRIENDS + "("
                + Contract.PRIMARY_KEY + " INTEGER PRIMARY KEY,"
                + Contract.NAME + " TEXT" + ")";

        db.execSQL(CREATE_PROFILE_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_FRIENDS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_ALL_FRIENDS_TABLE);
        db.execSQL(CREATE_FRIEND_REQUEST_TABLE);
        db.execSQL(CREATE_PENDING_FRIENDS_TABLE);
        db.execSQL(CREATE_BUFFER_COURSE_TABLE);
        db.execSQL(CREATE_FRIENDS_BUFFER_TABLE);

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