package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;

public class TimeLine {
    private Drawable icon;
    private String title;
    private String writer;
    private String date;
    private String time;
    private String content;

    public TimeLine() {
        writer = "writer";
        title = "title";
        content = "content";
        date = "2019-04-01";
        time = "하루 종일";
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getWriter(){
        return writer;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }
}
