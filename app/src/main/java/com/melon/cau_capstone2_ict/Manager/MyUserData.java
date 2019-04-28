package com.melon.cau_capstone2_ict.Manager;

import android.location.Location;

public class MyUserData {
    private String nickname = "테스트용";
    private String id = "test0000";
    private String residence = "상도";
    private Location location;
    private String currentBuilding = "";
    private String prevBuilding = "";
    private static MyUserData instance = null;


    public void setData(String id,String name){
        this.id = id;
        nickname = name;
    }
    public void setLocation(Location location){
        this.location = location;
    }

    public void setPrevBuilding(String prevBuilding) {
        this.prevBuilding = prevBuilding;
    }

    public String getPrevBuilding() {
        return prevBuilding;
    }

    public void setCurrentBuilding(String currentBuilding) {
        this.currentBuilding = currentBuilding;
    }

    public String getCurrentBuilding() {
        return currentBuilding;
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
