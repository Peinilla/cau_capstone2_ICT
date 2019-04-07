package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;

public class MyBoard {
    private Drawable icon;
    private String title;
    private String writer;
    private String date;
    private int numComment;
    private int numRecommend;

    public MyBoard(){
        title = "Test Title";
        writer = "test12345";
        date = "19.04.07";
        numComment = 3;
        numRecommend = 5;
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

    public int getNumComment() {
        return numComment;
    }

    public int getNumRecommend() {
        return numRecommend;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }
}
