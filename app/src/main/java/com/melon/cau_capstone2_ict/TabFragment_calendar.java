package com.melon.cau_capstone2_ict;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.FragmentAdapter;
import com.melon.cau_capstone2_ict.Manager.MyCalendar;
import com.melon.cau_capstone2_ict.Manager.MyCalendarAdapter;
import com.melon.cau_capstone2_ict.Manager.TimeLine;
import com.melon.cau_capstone2_ict.Manager.TimeLineAdapter;
import com.melon.cau_capstone2_ict.widget.CalendarItemView;
import com.melon.cau_capstone2_ict.widget.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class TabFragment_calendar extends Fragment implements CalendarFragment.OnFragmentListener {
    //    private String boardID;
    private String date;
    private Switch mSwitch;

    private RecyclerView myRecyclerView;
    private MyCalendarAdapter myCalendarAdapter;
    private LinearLayoutManager myLayoutManager;
    private HashMap<String, ArrayList<MyCalendar>> mMap;

    private RecyclerView timelineRecyclerView;
    private TimeLineAdapter timelineTimeLineAdapter;
    private ArrayList<TimeLine> timelineArray;
    private LinearLayoutManager timelineLayoutManager;

    private static final int COUNT_PAGE = 12;
    private ViewPager pager;
    private TextView textDate;
    private TextView textMonth;
    private FragmentAdapter adapter;
    private ArrayList<String> arrayList;

    Set<String> dates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        boardID = getArguments().getString("boardID");
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        mSwitch = rootView.findViewById(R.id.calendar_switch);
        myRecyclerView = rootView.findViewById(R.id.recycler_my);
        timelineRecyclerView = rootView.findViewById(R.id.recycler_timeline);
        pager = rootView.findViewById(R.id.calendar_pager);
        textDate = rootView.findViewById(R.id.text_calendar_date);
        textMonth = rootView.findViewById(R.id.text_calendar_title);

        initList();

        textDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshFragment();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                textMonth.setText(adapter.getMonthDisplayed(position));

                if (position == 0) {
                    adapter.addPrev();
                    pager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == adapter.getCount() - 1) {
                    adapter.addNext();
                    pager.setCurrentItem(adapter.getCount() - (COUNT_PAGE + 1), false);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timelineRecyclerView.setVisibility(View.GONE);
                    myRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    myRecyclerView.setVisibility(View.GONE);
                    timelineRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }
        // TODO: addEvent 구현 -> 캘린더에 setEvent
//    public void addEvent(){
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager();
//    }

    public void setEvent() {
        String date = toString().valueOf(textDate.getText());
        Log.d("getChild", Integer.toString(pager.getChildCount()));
        if (!arrayList.isEmpty() && pager.getChildCount() != 0) {
            for (int i = 0; i < pager.getChildCount(); i++) {
                if (((CalendarView) pager.getChildAt(i)).getChildCount() != 0) {
                    for (int j = 7; j < ((CalendarView) pager.getChildAt(i)).getChildCount(); j++) {
                        if (arrayList.size() == 0)
                            break;
                        CalendarItemView child = ((CalendarItemView) ((CalendarView) pager.getChildAt(i)).getChildAt(j));
                        String date1 = adapter.getDateDisplayed(child.getDate());
                        if (arrayList.contains(date1)) {
                            child.setIsEvent(true);
                            child.invalidate();
                        }
                    }
                }
            }
        }
        textDate.setText(date);
    }

    public void initList() {
        adapter = new FragmentAdapter(getChildFragmentManager());
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth(COUNT_PAGE);
        pager.setAdapter(adapter);
        pager.setCurrentItem(COUNT_PAGE);
        textDate.setText(adapter.getDateDisplayed(Calendar.getInstance().getTimeInMillis()));
        textMonth.setText(adapter.getMonthDisplayed(pager.getCurrentItem()));

        //         My Calendar
        mMap = new HashMap<String, ArrayList<MyCalendar>>();
        date = "2019.04.01";
        mMap.put(date, new ArrayList<MyCalendar>());
        mMap.get(date).add(new MyCalendar("리스트1", "내용"));
        mMap.get(date).add(new MyCalendar("리스트2", "내용"));

        date = "2019.04.08";
        mMap.put(date, new ArrayList<MyCalendar>());
        mMap.get(date).add(new MyCalendar("리스트4", "내용"));
        mMap.get(date).add(new MyCalendar("리스트5", "내용"));
        mMap.get(date).add(new MyCalendar("리스트6", "내용"));
        mMap.get(date).add(new MyCalendar("리스트9", "내용"));
        mMap.get(date).add(new MyCalendar("리스트10", "내용"));
        mMap.get(date).add(new MyCalendar("리스트11", "내용"));
        mMap.get(date).add(new MyCalendar("리스트12", "내용"));
        mMap.get(date).add(new MyCalendar("리스트4", "내용"));
        mMap.get(date).add(new MyCalendar("리스트5", "내용"));
        mMap.get(date).add(new MyCalendar("리스트6", "내용"));
        mMap.get(date).add(new MyCalendar("리스트4", "내용"));
        mMap.get(date).add(new MyCalendar("리스트5", "내용"));
        mMap.get(date).add(new MyCalendar("리스트6", "내용"));

        date = "2019.04.09";
        mMap.put(date, new ArrayList<MyCalendar>());
        mMap.get(date).add(new MyCalendar("리스트3", "내용"));

        date = "2019.04.17";
        mMap.put(date, new ArrayList<MyCalendar>());
        mMap.get(date).add(new MyCalendar("리스트7", "내용"));

        date = "2019.04.20";
        mMap.put(date, new ArrayList<MyCalendar>());
        mMap.get(date).add(new MyCalendar("리스트8", "내용"));

        arrayList = new ArrayList<String>();

        for (String m : mMap.keySet()) {
            if (!arrayList.contains(m))
                arrayList.add(m);
        }

        // Timeline
        timelineArray = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String title = "리스트";
            if (i < 10)
                title += "0";
            title += Integer.toString(i);
            timelineArray.add(new TimeLine(title, "이름 시간 장소"));
        }

        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myCalendarAdapter = new MyCalendarAdapter(getActivity(), mMap.get(date));
        myRecyclerView.setAdapter(myCalendarAdapter);

        timelineLayoutManager = new LinearLayoutManager(getActivity());
        timelineRecyclerView.setLayoutManager(timelineLayoutManager);
        timelineTimeLineAdapter = new TimeLineAdapter(getActivity(), timelineArray);
        timelineRecyclerView.setAdapter(timelineTimeLineAdapter);

        myRecyclerView.setVisibility(View.GONE);
    }

    public void refreshFragment() {
        date = toString().valueOf(textDate.getText());
        Log.d("date", date);

        if (!mMap.containsKey(date)) {
            Log.d("CreateArray: ", date);
            mMap.put(date, new ArrayList<MyCalendar>());
        }
        myCalendarAdapter.setRecyclerAdapter(mMap.get(date));
        myRecyclerView.setAdapter(myCalendarAdapter);
        if (mSwitch.isChecked()) {
            myRecyclerView.setVisibility(View.VISIBLE);
        } else {
            myRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFragmentListener(View view) {
        resizeHeight(view);
        setEvent();
    }

    public void resizeHeight(View rootView) {
        if (rootView.getHeight() < 1) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();

        if (layoutParams.height <= 0) {
            layoutParams.height = rootView.getHeight();
            pager.setLayoutParams(layoutParams);
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(pager.getLayoutParams().height, rootView.getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                layoutParams.height = value;
                pager.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
