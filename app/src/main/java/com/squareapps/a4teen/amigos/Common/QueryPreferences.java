package com.squareapps.a4teen.amigos.Common;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by y-pol on 10/10/2017.
 */

public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_USERS_SEARCH_QUERY = "usersSearch";

    public static String getPrefSearchQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setPrefSearchQuery(Context context, String searchQuery) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, searchQuery)
                .apply();
    }

    public static String getPrefUsersSearchQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USERS_SEARCH_QUERY, null);
    }

    public static void setPrefUsersSearchQuery(Context context, String searchQuery) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USERS_SEARCH_QUERY, searchQuery)
                .apply();

    }
}
