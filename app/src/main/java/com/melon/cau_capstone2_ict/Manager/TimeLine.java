package com.melon.cau_capstone2_ict.Manager;

public class TimeLine {
    private String title;
    private String name;
    private String reply;
    private String recommend;
    private String date;
    private String content;

    public TimeLine(String title, String score) {
        this.title = title;
        this.content = content;
    }

    public String getTitle(){
        return title;
    }

    public String getName(){
        return name;
    }

    public String getReply(){
        return reply;
    }

    public String getRecommend(){
        return recommend;
    }

    public String getDate() {
        return date;
    }

    public String getContent(){
        return content;
    }
}