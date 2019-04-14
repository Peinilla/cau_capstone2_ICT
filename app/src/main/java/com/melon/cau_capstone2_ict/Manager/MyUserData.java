package com.melon.cau_capstone2_ict.Manager;

public class MyUserData {
    private String nickname;
    private String id;
    private String residence;
    private  static MyUserData instance = null;


    public void setData(String id,String name){
        this.id = id;
        nickname = name;

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
