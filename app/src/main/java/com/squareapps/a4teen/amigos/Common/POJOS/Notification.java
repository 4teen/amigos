package com.squareapps.a4teen.amigos.Common.POJOS;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.squareapps.a4teen.amigos.BR;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification extends BaseObservable {

    private String body, from, tag, icon, title, type;

    private Long timeStamp;

    @Bindable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        notifyPropertyChanged(BR.body);
    }

    @Bindable
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
        notifyPropertyChanged(BR.from);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

}
