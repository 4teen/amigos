package com.square.apps.amigos;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class AppControl {

    public static final String TAG = "appcontrol";
    // Sender constants
    public static final  String NAME                       = "name";
    public static final  String GET_COURSES_TAG            = "getCourses";
    public static final  String ADD_COURSE_TAG             = "addCourse";
    public static final  String REMOVE_COURSE_TAG          = "removeCourse";
    public static final  String DELETEFRIEND_TAG           = "deleteFriend";
    public static final  String RETRIEVE_BUFFERED_MESSAGES = "retrieveBMessages";
    /**
     * Server API urls
     **/
    public static final  String URL_INDEX                  = "http://ec2-52-25-185-212.us-west-2.compute.amazonaws.com/amigos/amigos_login_api/";
    public static final  String URL_SEND                   = "http://ec2-52-25-185-212.us-west-2.compute.amazonaws.com/amigos/amigos_login_api/include/SEND_MESSAGE.PHP";
    /**
     * GCM CONSTANTS
     **/
    private static final String LOGGED_IN                  = "is_logged_in";
    private static AppControl sAppControl;
    private        Context    mAppContext;


    private AppControl(Context appContext){
        this.mAppContext = appContext;
    }

    public AppControl(){
    }

    public static synchronized AppControl getInstance(Context appContext){
        if (sAppControl == null) {
            sAppControl = new AppControl(appContext);
        }
        return sAppControl;
    }

    public boolean is_logged_in(){
        return PreferenceManager.getDefaultSharedPreferences(mAppContext).getBoolean(LOGGED_IN, false);
    }

    @Nullable
    public String getUserEmail(){
        return PreferenceManager.getDefaultSharedPreferences(mAppContext).getString(Contract.EMAIL, null);
    }

}



