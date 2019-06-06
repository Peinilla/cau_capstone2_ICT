package com.melon.cau_capstone2_ict.Manager;

public class MyFriendinfo {
    private String nickname;
    private String id;
    private int type;

    MyFriendinfo(String nickname, String id, int type){
        this.nickname = nickname;
        this.id = id;
        this.type = type;
    }
    public String getNickname() {
        return nickname;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setType(int type) {
        this.type = type;
    }
}
