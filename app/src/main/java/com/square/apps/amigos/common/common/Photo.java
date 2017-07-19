package com.square.apps.amigos.common.common;

/**
 * Created by y-pol on 7/18/2017.
 */

public class Photo {
    private String photoUrl;
    private String owner;
    private String groupId;

    public Photo() {
    }

    public Photo(String photoUrl, String owner, String groupId) {
        this.photoUrl = photoUrl;
        this.owner = owner;
        this.groupId = groupId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
