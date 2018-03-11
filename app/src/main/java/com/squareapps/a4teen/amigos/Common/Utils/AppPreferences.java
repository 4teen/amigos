package com.squareapps.a4teen.amigos.Common.Utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.R;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.NEW_MESSAGE_NOTIFICATION;
import static com.squareapps.a4teen.amigos.Common.Contract.User.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.User.NAME;
import static java.io.File.separator;

public class AppPreferences {

    private static final String PREF_USER_EMAIL = "email";


    public static void setPrefGroupNewMessageNotification(final Context context, final String groupId, final String notification) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance()
                .getReference(GROUPS + separator + groupId + separator + NEW_MESSAGE_NOTIFICATION);

        dataRef.setValue(notification, (databaseError, databaseReference) -> PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(groupId + NEW_MESSAGE_NOTIFICATION, notification)
                .apply());

    }

    public static String getPrefGroupNewMessageNotification(final Context context, final String groupId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(groupId + NEW_MESSAGE_NOTIFICATION, context.getString(R.string.pref_ringtone_silent));

    }

    public static String getPrefGroupAvatarUrl(final Context context, final String groupId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(groupId + AVATAR_URL, "");

    }

    public static void setPrefGroupAvatarUrl(final Context context, final String groupId, final String avatarUrl) {

        DatabaseReference dataRef = FirebaseDatabase.getInstance()
                .getReference(GROUPS + separator + groupId + separator + AVATAR_URL);

        dataRef.setValue(avatarUrl, (databaseError, databaseReference) -> PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(groupId + AVATAR_URL, avatarUrl)
                .apply());

    }


    public static String getPrefGroupName(final Context context, final String groupId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(groupId + NAME, "");

    }

    public static void setPrefGroupName(final Context context, final String groupId, final String groupName) {

        DatabaseReference dataRef = FirebaseDatabase.getInstance()
                .getReference(GROUPS + separator + groupId + separator + NAME);

        dataRef.setValue(groupName, (databaseError, databaseReference) -> PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(groupId + NAME, groupName)
                .apply());

    }


    public static String getPrefUserEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_EMAIL, null);
    }

    public static void setPrefUserEmail(Context context, String email) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_EMAIL, email)
                .apply();
    }

    public static String getPrefDisplayName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_display_name), null);
    }

    public static void setPrefDisplayName(Context context, String name) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.key_display_name), name)
                .apply();
    }

    public static String getPrefGender(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_gender), null);
    }


}
