package com.melon.cau_capstone2_ict.Manager;

public class TimeLine {
    private String title;
    private String name;
    private int reply;
    private int recommend;
    private String text;

    public TimeLine(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public TimeLine() {
        title = "title";
        name = "writer";
        reply = 0;
        recommend = 0;
        text = "text";
    }

    public String getTitle(){
        return title;
    }

    public String getName(){
        return name;
    }

    public int getReply(){
        return reply;
    }

    public int getRecommend(){
        return recommend;
    }

    public String getText(){
        return text;
    }
}