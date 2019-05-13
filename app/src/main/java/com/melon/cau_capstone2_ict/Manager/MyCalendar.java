package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class MyCalendar implements Serializable {
    private int _id;
    private Drawable icon;
    private String title;
    private String writer;
    private String date;
    private String content;

    public MyCalendar(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public MyCalendar() {
        _id = 0;
        writer = "writer";
        title = "title";
        content = "content";
        date = "2019-04-01";
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
}
