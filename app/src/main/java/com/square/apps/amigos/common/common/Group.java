package com.square.apps.amigos.common.common;

/**
 * Created by y-pol on 6/30/2017.
 */

public class Group {
    private String name;

    public Group(){

    }

    public Group(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
