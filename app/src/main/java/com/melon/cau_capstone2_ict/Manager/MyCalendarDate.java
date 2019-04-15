package com.melon.cau_capstone2_ict.Manager;

public class MyCalendarDate {
    private int year;
    private int month;
    private int day;
    private  static MyCalendarDate instance = null;

    public void setDate(int y, int m, int d){
        year = y;
        month = m;
        day = d;
    }
    public boolean isToday(int y, int m, int d){
        if(year == y && month == m && day == d){
            return true;
        }else{
            return false;
        }
    }

    public static synchronized MyCalendarDate getInstance(){
        if(instance == null){
            instance = new MyCalendarDate();
            instance.setDate(2000,1,1);
        }
        return instance;
    }
}
