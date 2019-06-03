package com.melon.cau_capstone2_ict.Manager;

public class MyReply {
    private static final int VIEW_MY = 0 ;
    private static final int VIEW_YOU = 1 ;

    private String name;
    private String id;
    private String text;
    private int index;
    private String time;
    private String postid;
    private int type;

    public MyReply(){
        type = VIEW_YOU;
    }

    public int getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
        if(id.equals(MyUserData.getInstance().getId())){
            type = VIEW_MY;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
