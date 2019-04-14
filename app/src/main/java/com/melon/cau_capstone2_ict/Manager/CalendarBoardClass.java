package com.melon.cau_capstone2_ict.Manager;

public class CalendarBoardClass {
    private int year;
    private int month;
    private int day;
    private String title;
    private String score;

    public CalendarBoardClass(String title, String score) {
        this.title = title;
        this.score = score;
    }

    public CalendarBoardClass(String title, String score,int y,int m, int d) {
        this.title = title;
        this.score = score;
        year = y;
        month = m;
        day = d;
    }
    public int[] getDate(){
        int[] res = {year,month,day};
        return res;
    }

    public String getTitle(){
        return title;
    }

    public String getScore(){
        return score;
    }
}
