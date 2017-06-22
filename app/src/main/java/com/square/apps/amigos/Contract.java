package com.square.apps.amigos;

import android.net.Uri;

public class Contract {

    //DATABASE FINAL STRINGS
    public static final int    DATABASE_VERSION         = 1;
    public static final String DATABASE_NAME            = "databaseAmigos";
    public static final String PRIMARY_KEY              = "_id";
    public static final String NAME                     = "name";
    public static final String EMAIL                    = "email";
    //table friends unique strings
    public static final String TABLE_FRIENDS            = "friends";
    public static final String COUNT                    = "count";
    //Table user unique strings
    public static final String TABLE_PROFILE            = "profile";
    public static final String STUDENT_ID               = "StudentID";
    public static final String UID                      = "uid";
    public static final String CREATED_AT               = "created_at";
    public static final String PROFILE_IMAGE            = "ProfilePic";
    public static final String UNIQUE_ID                = "unique_id";
    public static final String PROFILE_PIC              = "ProfilePic";
    public static final String REGID                    = "gcm_regid";
    //Table MESSAGES unique strings
    public static final String TABLE_MESSAGES           = "messages";
    public static final String MESSAGE                  = "message";
    public static final String FROM                     = "col_from";
    public static final String TO                       = "col_to";
    public static final String AT                       = "col_at";
    //Table Courses unique strings
    public static final String TABLE_COURSES            = "courses";
    public static final String COURSE_ID                = "CourseID";
    public static final String COLLEGE                  = "College";
    public static final String DEPARTMENT               = "Department";
    public static final String CRN                      = "CRN";
    public static final String SUBJECT_CRS              = "SubjectCRS";
    public static final String SECTION                  = "Section";
    public static final String TITLE                    = "Title";
    public static final String BUILDING                 = "Building";
    public static final String ROOM                     = "Room";
    public static final String INSTRUCTOR               = "Instructor";
    public static final String CAMPUS                   = "Campus";
    public static final String TIME                     = "Time";
    public static final String DAYS                     = "Days";
    //Table Friend_request unique strings
    public static final String TABLE_FRIEND_REQUESTS    = "friend_request_table";
    //Table pending_friends unique strings
    public static final String TABLE_PENDING_FRIENDS    = "pending_request_table";
    //Table Courses_buffer unique strings
    public static final String TABLE_COURSES_BUFFER     = "courses_buffer";
    public static final String TABLE_FRIENDS_BUFFER     = "friends_buffer";
    //table All_friends unique strings
    public static final String TABLE_ALL_FRIENDS        = "all_friends";
    //Content provider Uris
    public static final Uri    CONTENT_URI_MESSAGES     = Uri.parse ( "content://com.square.apps.provider/message" );
    public static final Uri    CONTENT_URI_PROFILE      = Uri.parse ( "content://com.square.apps.provider/profile" );
    public static final Uri    CONTENT_URI_FRIENDS      = Uri.parse ( "content://com.square.apps.provider/friends" );
    public static final Uri    CONTENT_URI_COURSES      = Uri.parse ( "content://com.square.apps.provider/courses" );
    public static final Uri    CONTENT_URI_GCMUSERS     = Uri.parse ( "content://com.square.apps.provider/gcm_users" );
    //Global Contants
    public static final String PASSWORD                 = "password";
    //PubNub
    public static final String PUB_NUB_PUBLISH_KEY      = "pub-c-f55c194c-1f1e-4e45-a41d-cad63d1a3f40";
    public static final String PUB_NUB_SUBSCRIBE_KEY    = "sub-c-24fe291a-7952-11e5-a643-02ee2ddab7fe";
    //Services Contants
    public static final String LOGIN_COMPLETE           = "login_complete";
    public static final String SEND_COMPLETE            = "send_complete";
    public static final String GETTING_COURSES_COMPLETE = "Done_getting_courses";
    public static final String SEARCH_COURSES_COMPLETE  = "Done_searching_courses";
    public static final String COUNT_UPDATE_COMPLETE    = "Count_update_complete";
    //tags
    public static final String TAG_SHOW_FRIEND_REQUESTS = "showFriendRequests";

    private Contract(){

    }

}
