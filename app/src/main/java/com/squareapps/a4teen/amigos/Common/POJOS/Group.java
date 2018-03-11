package com.squareapps.a4teen.amigos.Common.POJOS;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.Exclude;
import com.squareapps.a4teen.amigos.BR;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Group extends BaseObservable {
    private String groupId;
    private String name;
    private String owner;
    private String message;
    private Long timeStamp;
    private String avatarUrl;
    private HashMap<String, Object> members;

    public Group() {

    }

    public Group(String name, String owner, String groupId) {
        this.name = name;
        this.owner = owner;
        this.groupId = groupId;
    }


    public HashMap<String, Object> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, Object> members) {
        this.members = members;
    }

    @Bindable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatarUrl);
    }

    @Bindable
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        notifyPropertyChanged(BR.owner);
    }

    @Bindable
    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
        notifyPropertyChanged(BR.timeStamp);
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String id) {
        this.groupId = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

}
