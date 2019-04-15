package com.melon.cau_capstone2_ict.Manager;

import android.graphics.drawable.Drawable;


public class MyChat {

    private int type;
    private Drawable icon;
    private String text;
    private String writer;
    private String date;

    public MyChat(int t){
        type = t;
        text = "Test text";
        writer = "test12345";
        date = "19.04.07";
    }


    public void setType(int t){type = t;}

    public void setText(String text) {
        this.text = text;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType(){return type;}

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getWriter() {
        return writer;
    }
}
