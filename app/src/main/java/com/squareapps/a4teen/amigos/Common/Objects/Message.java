package com.squareapps.a4teen.amigos.Common.Objects;

public class Message {
    private String senderId;
    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;
    private String timeStamp;

    public Message() {
    }

    public Message(String senderId, String id, String text, String name, String photoUrl, String imageUrl) {
        this.senderId = senderId;
        this.id = id;
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl; //my profile pic
        this.imageUrl = imageUrl;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
