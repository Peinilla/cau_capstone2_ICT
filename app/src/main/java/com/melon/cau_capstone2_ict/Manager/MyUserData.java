package com.melon.cau_capstone2_ict.Manager;

import android.location.Location;
import java.util.ArrayList;

public class MyUserData {
    private String nickname = "테스트용";
    private String id = "debugUser";
    private String residence = "상도";
    private Location location;
    private String currentBuilding = "";
    private String prevBuilding = "";
    private String nowimTag = "";
    private String email = "";
    private String birth = "";
    private String major = "";
    private String hobby = "";
    private String Bop = "0";
    private ArrayList<String> friendList;
    private static MyUserData instance = null;

    private MyUserData(){
        friendList = new ArrayList<>();
    }

    public void clear(){
        currentBuilding = "";
        prevBuilding = "";
        nowimTag = "";
        email = "";
        birth = "";
        major = "";
        hobby = "";
        Bop = "0";
    }
    public void clearFriendList(){
        friendList.clear();
    }
    public void addFriend(String f){
        friendList.add(f);
    }
    public ArrayList getFriendList(){
        return friendList;
    }

    public void setNowimTag(String nowimTag) {
        this.nowimTag = nowimTag;
    }

    public String getBirth() {
        return birth;
    }

    public String getEmail() {
        return email;
    }

    public String getHobby() {
        return hobby;
    }

    public String getMajor() {
        return major;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getBop() {
        return Bop;
    }

    public void setBop(String bop) {
        Bop = bop;
    }

    public String getNowimTag() {
        return nowimTag;
    }

    public void setData(String id, String name){
        this.id = id;
        nickname = name;
        Bop = "0";
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
