package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    /**
     * Database가 존재하지 않을 때, 딱 한번 실행된다.
     * DB를 만드는 역할을 한다.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // String 보다 StringBuffer가 Query 만들기 편하다.
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE TEST_TABLE ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TITLE TEXT, ");
        sb.append(" CONTENT TEXT, "); // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());
        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    /**
     * Application의 버전이 올라가서 * Table 구조가 변경되었을 때 실행된다.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    /** * */
    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addCalendar(MyCalendar myCalendar) {
        // 1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase db = getWritableDatabase();
        // 2. Person Data를 Insert한다.
        // _id는 자동으로 증가하기 때문에 넣지 않습니다.
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" TITLE, CONTENT ) ");
        sb.append(" VALUES ( ?, ? ) ");
        // sb.append(" VALUES ( #NAME#, #AGE#, #PHONE# ) "); // //
        // Age는 Integer이기 때문에 홀따옴표(')를 주지 않는다.
        // String query = sb.toString();
        // query.replace("#NAME#", "'" + person.getName() + "'");
        // query.replace("#NAME#", person.getAge());
        // query.replace("#NAME#", "'" + person.getPhone() + "'"); //
        // db.execSQL(query);
        db.execSQL(sb.toString(), new Object[]{
                myCalendar.getTitle(),
                myCalendar.getContent()
        });
        ;
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public List getAllMyCalendars() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, TITLE, CONTENT FROM TEST_TABLE ");
        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List myCalendars = new ArrayList();
        MyCalendar myCalendar = null;
        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            myCalendar = new MyCalendar();
            myCalendar.set_id(cursor.getInt(0));
            myCalendar.setTitle(cursor.getString(1));
            myCalendar.setContent(cursor.getString(2));
            myCalendars.add(myCalendar);
        }
        return myCalendars;
    }

}

