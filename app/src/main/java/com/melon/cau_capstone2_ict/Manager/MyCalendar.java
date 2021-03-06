package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class MyCalendar {
    private int _id;
    private Drawable icon;
    private String title;
    private String writer;
    private String content;
    private String date;
    private String time;
    private String color;

    public MyCalendar() {
        _id = 0;
        writer = "writer";
        title = "title";
        content = "content";
        date = "2019-04-01";
        time = "하루 종일";
        color = "black";
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public void setColor(String color){
        this.color = color;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getColor(){
        return color;
    }
}
