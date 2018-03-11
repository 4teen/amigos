package com.squareapps.a4teen.amigos.Common.POJOS;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.squareapps.a4teen.amigos.BR;

public class Photo extends BaseObservable {
    private String photoUrl;
    private String owner;
    private String groupId;
    private String id;
    private long timeStamp;

    public Photo() {
    }

    public Photo(String photoUrl, String owner, String groupId, String id) {
        this.photoUrl = photoUrl;
        this.owner = owner;
        this.groupId = groupId;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Bindable
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        notifyPropertyChanged(BR.photoUrl);
    }

    @Bindable
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        notifyPropertyChanged(BR.owner);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPhotoFilename() {
        return "IMG_" + getClass().toString() + ".jpg";
    }
}
