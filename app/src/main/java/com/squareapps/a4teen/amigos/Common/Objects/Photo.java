package com.squareapps.a4teen.amigos.Common.Objects;


import java.util.HashMap;
import java.util.Map;

public class Photo {
    private String photoUrl;
    private String owner;
    private String groupId;
    private String id;
    private Map<String, String> timeStamp;

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

    public Map<String, String> getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Map<String, String> timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getPhotoFilename() {
        return "IMG_" + getClass().toString() + ".jpg";
    }
}
