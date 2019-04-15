package com.melon.cau_capstone2_ict;

public class CalendarBoardClass {
    private String title;
    private String score;

    CalendarBoardClass(String title, String score) {
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
