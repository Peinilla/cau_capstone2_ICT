package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.RecyclerAdapter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TabFragment_calendar extends Fragment {
    private CalendarView calendarView;
    private TextView textView_date;

    private RecyclerView myRecyclerView;
    private RecyclerAdapter myRecyclerAdapter;
    private ArrayList<CalendarBoardClass> myArray;
    private LinearLayoutManager myLayoutManager;

    private RecyclerView timelineRecyclerView;
    private RecyclerAdapter timelineRecyclerAdapter;
    private ArrayList<CalendarBoardClass> timelineArray;
    private LinearLayoutManager timelineLayoutManager;

    private Calendar calendar;
    private int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        calendarView = rootView.findViewById(R.id.calendar);
        textView_date = rootView.findViewById(R.id.textview_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        String date = year + "/" + Integer.toString((int) month + 1) + "/" + day;
        textView_date.setText(date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                String date = year + "/" + Integer.toString((int) month + 1) + "/" + day;
                textView_date.setText(date);
            }
        });

        myArray = new ArrayList<>();
        myArray.add(new CalendarBoardClass("운동", "2시 305관"));
        myArray.add(new CalendarBoardClass("친구들", "7시 흑석역"));
        myRecyclerView = rootView.findViewById(R.id.recycler_my);
        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerAdapter = new RecyclerAdapter(getActivity(), myArray);
        myRecyclerView.setAdapter(myRecyclerAdapter);

        timelineArray = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String title = "리스트";
            if (i < 10)
                title += "0";
            title += Integer.toString(i);
            timelineArray.add(new CalendarBoardClass(title, "이름 시간 장소"));
        }

        timelineRecyclerView = rootView.findViewById(R.id.recycler_timeline);
        timelineLayoutManager = new LinearLayoutManager(getActivity());
        timelineRecyclerView.setLayoutManager(timelineLayoutManager);
        timelineRecyclerAdapter = new RecyclerAdapter(getActivity(), timelineArray);
        timelineRecyclerView.setAdapter(timelineRecyclerAdapter);

        return rootView;

    }
}

