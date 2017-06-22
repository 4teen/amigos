package com.square.apps.amigos.common.common.Friend;


import android.support.annotation.NonNull;


public class Friend {
    private String friendID;
    private String name;
    private String email;
    private String count;

    public void setCount(String count) {
        this.count = count;
    }

    public String getEmail() {
        return email;
    }

    /**
     * setters
     **/
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePic() {
    }

    @NonNull
    @Override
    public String toString() {
        return friendID + "\n" + name + "\n" + email + "\n" + count;
    }

}
