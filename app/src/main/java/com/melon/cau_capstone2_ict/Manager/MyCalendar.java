package com.melon.cau_capstone2_ict.Manager;

public class MyCalendar {
    private String title;
    private String name;
    private String reply;
    private String recommend;
    private String content;

    public MyCalendar(String title, String content) {
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

    public String getContent(){
        return content;
    }
}
