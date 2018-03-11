package com.squareapps.a4teen.amigos.Common.POJOS;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.squareapps.a4teen.amigos.BR;


public class Message extends BaseObservable {
    private String senderId;
    private String id;
    private String text;
    private String name;
    private String avatarUrl;
    private String imageUrl;
    private int viewType;
    private long timeStamp;

    public Message() {

    }

    public Message(String senderId, String id, String text, String name, String avatarUrl, String imageUrl) {
        this.senderId = senderId;
        this.id = id;
        this.text = text;
        this.name = name;
        this.avatarUrl = avatarUrl; //my profile pic
        this.imageUrl = imageUrl;
    }

    @Bindable
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        notifyPropertyChanged(BR.avatarUrl);
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatarUrl);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    /*@Override
    public int getType() {
        Log.d("getTag", String.valueOf(viewType));
        if (viewType != 0)
            return viewType;
        else
            return 0;
    }*/
}
