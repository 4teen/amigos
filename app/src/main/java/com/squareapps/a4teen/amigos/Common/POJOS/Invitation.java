package com.squareapps.a4teen.amigos.Common.POJOS;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import com.squareapps.a4teen.amigos.BR;

import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_NAME;


public class Invitation extends BaseObservable {
    private String groupId, groupName, groupAvatarUrl, senderUid, senderName, senderAvatarUrl, status;
    private Long timeStamp;

    public Invitation() {
    }

    public Invitation(Bundle bundle) {
        groupId = bundle.getString(GROUP_ID);
        groupName = bundle.getString(GROUP_NAME);
        groupAvatarUrl = bundle.getString("groupAvatar");
        senderUid = bundle.getString("senderUid");
        senderName = bundle.getString("senderName");
        senderAvatarUrl = bundle.getString("senderAvatarUrl");
        status = bundle.getString("status");

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAvatarUrl() {
        return groupAvatarUrl;
    }

    public void setGroupAvatarUrl(String groupAvatarUrl) {
        this.groupAvatarUrl = groupAvatarUrl;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

}
