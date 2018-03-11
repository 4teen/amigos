package com.squareapps.a4teen.amigos.Common.POJOS;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import com.google.firebase.database.Exclude;
import com.squareapps.a4teen.amigos.BR;
import com.squareapps.a4teen.amigos.Common.Utils.QueryPreferences;
import com.squareapps.a4teen.amigos.R;

import java.util.Locale;


public class User extends BaseObservable {

    private String id;
    private boolean inApp;
    private String name;
    private String email;
    private String avatarUrl;
    private String phoneNumber;
    private String school;
    private String gender;

    @Exclude
    private String birthdate;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    public boolean isInApp() {
        return inApp;
    }

    public void setInApp(boolean inApp) {
        this.inApp = inApp;
    }

    @Bindable
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
        notifyPropertyChanged(BR.school);
    }

    @Bindable
    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
        notifyPropertyChanged(BR.birthdate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    @Bindable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatarUrl);
    }

    @Override
    public String toString() {
        return name + phoneNumber + id;
    }


    public SpannableString getHighlightedNameTextSpan(Context context) {

        TextAppearanceSpan highlightTextSpan = new TextAppearanceSpan(context, R.style.searchTextHiglight);

        final int startIndex = indexOfSearchQuery(context);

        if (startIndex == -1) {
            // If the user didn't do a search, or the search string didn't match a display
            // name, show the display name without highlighting
            return null;

        } else {
            // If the search string matched the display name, applies a SpannableString to
            // highlight the search string with the displayed display name

            // Wraps the display name in the SpannableString
            final SpannableString highlightedName = new SpannableString(name);

            // Sets the span to start at the starting point of the match and end at "length"
            // characters beyond the starting point
            highlightedName.setSpan(highlightTextSpan, startIndex,
                    startIndex + QueryPreferences.getPrefUsersSearchQuery(context).length(), 0);

            return highlightedName;

        }


    }


    private int indexOfSearchQuery(Context context) {

        if (!TextUtils.isEmpty(QueryPreferences.getPrefUsersSearchQuery(context))) {

            return name.toLowerCase(Locale.getDefault()).indexOf(

                    QueryPreferences
                            .getPrefUsersSearchQuery(context)
                            .toLowerCase(Locale.getDefault()));
        }
        return -1;
    }
}



