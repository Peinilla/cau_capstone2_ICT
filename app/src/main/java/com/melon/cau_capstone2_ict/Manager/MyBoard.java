package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyBoard implements Serializable {
    private Drawable icon;
    private String title;
    private String writer;
    private String date;
    private String text;
    private int numComment;
    private int numRecommend;
    private String postId;
    private boolean isBabtype = false;

    public MyBoard(){
        title = "Test Title";
        writer = "test12345";
        date = "19.04.07";
        text = "text";
        numComment = 0;
        numRecommend = 0;
    }

    public void setBabtype(boolean babtype) {
        isBabtype = babtype;
    }

    public boolean isBabtype() {
        return isBabtype;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
