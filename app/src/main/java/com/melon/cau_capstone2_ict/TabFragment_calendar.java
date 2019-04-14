package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.CalendarBoardClass;
import com.melon.cau_capstone2_ict.Manager.MyCalendarDate;
import com.melon.cau_capstone2_ict.Manager.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

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
    public static int mYear, mMonth, mDay;

    FloatingActionButton add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        calendarView = rootView.findViewById(R.id.calendar);
        textView_date = rootView.findViewById(R.id.textview_date);
        add = (FloatingActionButton)rootView.findViewById(R.id.button_calendaradd);
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
        String date = mYear + "/" + Integer.toString((int) mMonth + 1) + "/" + mDay;
        textView_date.setText(date);

        myArray = new ArrayList<>();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                MyCalendarDate.getInstance().setDate(year,month,day);
                myRecyclerAdapter.notifyDataSetChanged();

                String date = year + "/" + Integer.toString((int) month + 1) + "/" + day;
                textView_date.setText(date);
            }
        });

        CalendarBoardClass c = new CalendarBoardClass("할 일","test용",mYear,mMonth,mDay);
        myArray.add(c);

        myRecyclerView = rootView.findViewById(R.id.recycler_my);
        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerAdapter = new RecyclerAdapter(getActivity(), myArray);
        myRecyclerView.setAdapter(myRecyclerAdapter);

        timelineArray = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String title = "친구 타임라인";
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

        add.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("Tag", "click d : " +mDay);
                myRecyclerAdapter.additem(mYear,mMonth,mDay);
                myRecyclerAdapter.notifyDataSetChanged();
            }
        });

        return rootView;

    }
}

