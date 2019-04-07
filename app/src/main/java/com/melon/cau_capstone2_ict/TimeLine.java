package com.melon.cau_capstone2_ict;

public class TimeLine {
    private String title;
    private String score;

    TimeLine(String title, String score) {
        this.title = title;
        this.score = score;
    }

    public String getTitle(){
        return title;
    }

    public String getScore(){
        return score;
    }
}
