package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE CALENDAR_TABLE ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TITLE TEXT, ");
        sb.append(" CONTENT TEXT, ");
        sb.append(" DATE TEXT, ");
        sb.append(" TIME TEXT, ");
        sb.append(" COLOR TEXT )");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    public void calendarDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void add(MyCalendar myCalendar) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO CALENDAR_TABLE ( ");
        sb.append(" TITLE, CONTENT, DATE, TIME, COLOR ) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ? ) ");
        db.execSQL(sb.toString(), new Object[]{
                myCalendar.getTitle(),
                myCalendar.getContent(),
                myCalendar.getDate(),
                myCalendar.getTime(),
                myCalendar.getColor()
        });
    }

    // myCaledars: 서버, list: DB
    public void addList(ArrayList<MyCalendar> myCalendars){
        ArrayList<MyCalendar> list = getAllMyCalendars();

        for(MyCalendar myCalendar : myCalendars){
            if(!list.contains(myCalendar)){
                add(myCalendar);
            }
        }
        for(MyCalendar myCalendar : list){
            if(!myCalendars.contains(myCalendar)){
                delete(myCalendar.get_id());
            }
        }
    }

    public int size(){
        return getAllMyCalendars().size();
    }

    public void update(int _id, MyCalendar myCalendar){
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" UPDATE CALENDAR_TABLE SET ");
        sb.append(" TITLE = " + myCalendar.getTitle());
        sb.append(", CONTENT = " + myCalendar.getContent());
        sb.append(", DATE = " + myCalendar.getDate());
        sb.append(", TIME = " + myCalendar.getTime());
        sb.append(", COLOR = " + myCalendar.getColor());
        sb.append(" WHERE _ID = " + Integer.toString(_id));
        db.execSQL(sb.toString());
    }

    public void delete(int _id){
        StringBuffer sb = new StringBuffer();
        sb.append(" DELETE FROM CALENDAR_TABLE WHERE _ID = " + Integer.toString(_id));
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sb.toString());
    }

    public ArrayList<MyCalendar> getAllMyCalendars() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, TITLE, CONTENT, DATE, TIME, COLOR FROM CALENDAR_TABLE ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<MyCalendar> myCalendars = new ArrayList<>();
        MyCalendar myCalendar = null;

        while (cursor.moveToNext()) {
            myCalendar = new MyCalendar();
            myCalendar.set_id(cursor.getInt(0));
            myCalendar.setTitle(cursor.getString(1));
            myCalendar.setContent(cursor.getString(2));
            myCalendar.setDate(cursor.getString(3));
            myCalendar.setTime(cursor.getString(4));
            myCalendar.setColor(cursor.getString(5));
            myCalendars.add(myCalendar);
        }
        return myCalendars;
    }

    public ArrayList<String> getCalendarByMonth(String year, String month) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT DATE FROM CALENDAR_TABLE WHERE strftime('%Y', DATE) = ? AND strftime('%m', DATE) = ? ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{year + "", month + ""});

        ArrayList<String> myCalendars = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            if(!myCalendars.contains(date)){
                myCalendars.add(date);
            }
        }
        return myCalendars;
    }

    public ArrayList<MyCalendar> getCalendarByDate(String date) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM CALENDAR_TABLE WHERE DATE = ? ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{date + ""});

        ArrayList<MyCalendar> myCalendars = new ArrayList<>();
        MyCalendar myCalendar = null;

        while (cursor.moveToNext()) {
            myCalendar = new MyCalendar();
            myCalendar.set_id(cursor.getInt(0));
            myCalendar.setTitle(cursor.getString(1));
            myCalendar.setContent(cursor.getString(2));
            myCalendar.setDate(cursor.getString(3));
            myCalendar.setTime(cursor.getString(4));
            myCalendar.setColor(cursor.getString(5));
            myCalendar.setWriter(MyUserData.getInstance().getNickname());
            myCalendars.add(myCalendar);
        }
        return myCalendars;
    }

    public ArrayList<String> getCalendarByColor(String date){
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT COLOR FROM CALENDAR_TABLE WHERE DATE = ? ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{date + ""});

        ArrayList<String> colors = new ArrayList<>();

        while (cursor.moveToNext()) {
            colors.add(cursor.getString(0));
        }
        return colors;
    }
}

