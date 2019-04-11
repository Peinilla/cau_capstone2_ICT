package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TabFragment_calendar extends Fragment {
    // calendar variable
    private CalendarView calendarView;
    private TextView textView_date;
    private Calendar calendar;
    private int year, month, day;

    // my board
    private RecyclerView myRecyclerView;
    private RecyclerAdapter myRecyclerAdapter;
    private LinearLayoutManager myLayoutManager;
    private HashMap<String, ArrayList<CalendarBoardClass>> map;
    AppBarLayout appBarLayout;
    String date;

    // timeline
    private RecyclerView timelineRecyclerView;
    private RecyclerAdapter timelineRecyclerAdapter;
    private ArrayList<CalendarBoardClass> timelineArray;
    private LinearLayoutManager timelineLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        map = new HashMap<String, ArrayList<CalendarBoardClass>>();

        map.put("2019/4/1", new ArrayList<CalendarBoardClass>());
        map.put("2019/4/8", new ArrayList<CalendarBoardClass>());
        map.put("2019/4/9", new ArrayList<CalendarBoardClass>());
        map.put("2019/4/17", new ArrayList<CalendarBoardClass>());
        map.put("2019/4/20", new ArrayList<CalendarBoardClass>());
        map.get("2019/4/1").add(new CalendarBoardClass("리스트1", "시간 장소"));
        map.get("2019/4/1").add(new CalendarBoardClass("리스트2", "시간 장소"));
        map.get("2019/4/9").add(new CalendarBoardClass("리스트3", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트4", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트5", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트6", "시간 장소"));
        map.get("2019/4/17").add(new CalendarBoardClass("리스트7", "시간 장소"));
        map.get("2019/4/20").add(new CalendarBoardClass("리스트8", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트9", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트10", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트11", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트12", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트4", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트5", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트6", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트4", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트5", "시간 장소"));
        map.get("2019/4/8").add(new CalendarBoardClass("리스트6", "시간 장소"));

        // calendar
        calendarView = rootView.findViewById(R.id.calendar);
        textView_date = rootView.findViewById(R.id.textview_date);
        // today
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        date = year + "/" + Integer.toString(month + 1) + "/" + day;
        textView_date.setText(date);

        // date click
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                date = year + "/" + Integer.toString(month + 1) + "/" + day;
                textView_date.setText(date);

                if (!map.containsKey(date)) {
                    Log.d("CreateArray: ", date);
                    map.put(date, new ArrayList<CalendarBoardClass>());
                }
                myRecyclerAdapter.setRecyclerAdapter(map.get(date));
                myRecyclerView.setAdapter(myRecyclerAdapter);
                myRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        myRecyclerView = rootView.findViewById(R.id.recycler_my);
        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerAdapter = new RecyclerAdapter(getActivity(), map.get(date));
        myRecyclerView.setAdapter(myRecyclerAdapter);

        appBarLayout = rootView.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float ratio = 1 - Math.abs((float) verticalOffset) / (float) appBarLayout.getTotalScrollRange();
//                myRecyclerView.setAlpha(ratio);
            }
        });

        // timeline
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

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPause()", "Start");
    }
}

