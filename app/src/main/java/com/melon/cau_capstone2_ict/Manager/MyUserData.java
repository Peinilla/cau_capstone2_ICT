package com.melon.cau_capstone2_ict.Manager;

import android.location.Location;

public class MyUserData {
    private String nickname = "테스트용";
    private String id = "test0000";
    private String residence = "상도";
    private Location location;
    private String nearBuilding;
    private static MyUserData instance = null;


    public void setData(String id,String name){
        this.id = id;
        nickname = name;
    }
    public void setLocation(Location location){
        this.location = location;
    }

    public void setNearBuilding(String nearBuilding) {
        this.nearBuilding = nearBuilding;
    }

    public String getNearBuilding() {
        return nearBuilding;
    }

    public void setResidence(String s){
        residence = s;
    }

    public String getResidence() {
        return residence;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public static synchronized MyUserData getInstance(){
        if(instance == null){
            instance = new MyUserData();
        }
        return instance;
    }
}
