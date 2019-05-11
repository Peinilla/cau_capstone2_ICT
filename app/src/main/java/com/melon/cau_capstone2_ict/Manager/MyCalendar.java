package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;

public class MyCalendar {
    private Drawable icon;
    private String title;
    private String writer;
    private String date;
    private String text;
    private int numComment;
    private int numRecommend;

    public MyCalendar(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public MyCalendar() {
        title = "title";
        writer = "writer";
        date = "19.04.01";
        numComment = 0;
        numRecommend = 0;
        text = "text";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }

    public void setNumRecommend(int numRecommend) {
        this.numRecommend = numRecommend;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle(){
        return title;
    }

    public String getWriter(){
        return writer;
    }

    public String getDate(){
        return date;
    }

    public int getNumComment() {
        return numComment;
    }

    public int getNumRecommend() {
        return numRecommend;
    }

    public String getText(){
        return text;
    }
}
