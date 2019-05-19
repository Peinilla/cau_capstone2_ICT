package com.melon.cau_capstone2_ict;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.FragmentAdapter;
import com.melon.cau_capstone2_ict.Manager.MyCalendar;
import com.melon.cau_capstone2_ict.Manager.MyCalendarAdapter;
import com.melon.cau_capstone2_ict.Manager.RSSHandler;
import com.melon.cau_capstone2_ict.Manager.TimeLine;
import com.melon.cau_capstone2_ict.Manager.TimeLineAdapter;
import com.melon.cau_capstone2_ict.widget.CalendarItemView;
import com.melon.cau_capstone2_ict.widget.CalendarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TabFragment_calendar extends Fragment implements CalendarFragment.OnFragmentListener {
    private String date;
    private Switch mSwitch;

    private RecyclerView myRecyclerView;
    private MyCalendarAdapter myCalendarAdapter;
    private LinearLayoutManager myLayoutManager;
    private HashMap<String, ArrayList<MyCalendar>> mMap;

    private RecyclerView timelineRecyclerView;
    private TimeLineAdapter timeLineAdapter;
    private ArrayList<TimeLine> timelineArray;
    private LinearLayoutManager timelineLayoutManager;

    private static final int COUNT_PAGE = 12;
    private ViewPager pager;
    private TextView textDate;
    private TextView textMonth;
    private FragmentAdapter fragmentAdapter;
    private ArrayList<String> arrayList;

    // Test
    private FloatingActionButton fab_add;
    private FloatingActionButton fab_reload;
    private FrameLayout frameLayout;
    private AppBarLayout appBarLayout;

    // test
    ImageView weatherImage;
    TextView tempText;
    TextView popText;
    TextView ptyText;
    TextView wfText;
    LinearLayout linearLayout;
    private static String finalUrl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159060500";
    private static RSSHandler handler = new RSSHandler(finalUrl);
    String temp, wfKor, pty, pop;
    int hour;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Tag", "calendar start");

        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        mSwitch = (Switch) rootView.findViewById(R.id.calendar_switch);
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_my);
        timelineRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_timeline);
        pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);
        textDate = (TextView) rootView.findViewById(R.id.text_calendar_date);
        textMonth = (TextView) rootView.findViewById(R.id.text_calendar_title);

        // test
        fab_add = (FloatingActionButton) rootView.findViewById(R.id.button_calendar_add);
        fab_reload = (FloatingActionButton) rootView.findViewById(R.id.button_calendar_reload);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.calendar_board_container);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.calendar_appbar);

        // test
        weatherImage = (ImageView) rootView.findViewById(R.id.imageView_wfKor1);
        wfText = (TextView) rootView.findViewById(R.id.textView_wfKor1);
        tempText = (TextView) rootView.findViewById(R.id.textView_temp1);
        ptyText = (TextView) rootView.findViewById(R.id.textView_pty1);
        popText = (TextView) rootView.findViewById(R.id.textView_pop1);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_pty1);

        handler.fetchXML();
        while (handler.parsingComplete) ;
        weatherParse();

        initList();

        fab_add.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Test
                Fragment childFragment = new TabFragment_calendarWrite();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.calendar_board_container, childFragment).commit();
                appBarLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        fab_reload.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Test
                myCalendarAdapter.notifyDataSetChanged();
                timeLineAdapter.notifyDataSetChanged();
            }
        });

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
                textMonth.setText(fragmentAdapter.getMonthDisplayed(position));

                if (position == 0) {
                    fragmentAdapter.addPrev();
                    pager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == fragmentAdapter.getCount() - 1) {
                    fragmentAdapter.addNext();
                    pager.setCurrentItem(fragmentAdapter.getCount() - (COUNT_PAGE + 1), false);
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
                    timeLineAdapter.notifyDataSetChanged();
                } else {
                    myRecyclerView.setVisibility(View.GONE);
                    timelineRecyclerView.setVisibility(View.VISIBLE);
                    myCalendarAdapter.notifyDataSetChanged();
                }
            }
        });

//        weatherParse();

        return rootView;
    }

    public void setEvent() {
        arrayList = new ArrayList<String>();

        for (String m : mMap.keySet()) {
            if (!arrayList.contains(m) && mMap.get(m).size() != 0)
                arrayList.add(m);
        }

        String date = toString().valueOf(textDate.getText());
        Log.d("getChild", Integer.toString(pager.getChildCount()));
        if (!arrayList.isEmpty() && pager.getChildCount() != 0) {
            for (int i = 0; i < pager.getChildCount(); i++) {
                if (((CalendarView) pager.getChildAt(i)).getChildCount() != 0) {
                    for (int j = 7; j < ((CalendarView) pager.getChildAt(i)).getChildCount(); j++) {
                        if (arrayList.size() == 0)
                            break;
                        CalendarItemView child = ((CalendarItemView) ((CalendarView) pager.getChildAt(i)).getChildAt(j));
                        String date1 = fragmentAdapter.getDateDisplayed(child.getDate());
                        if (arrayList.contains(date1)) {
                            child.setIsEvent(true);
                            child.invalidate();
                            arrayList.remove(date1);
                        }
                    }
                }
            }
        }
        textDate.setText(date);
    }

    public void initList() {
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        fragmentAdapter.setOnFragmentListener(this);
        fragmentAdapter.setNumOfMonth(COUNT_PAGE);
        pager.setAdapter(fragmentAdapter);
        pager.setCurrentItem(COUNT_PAGE);
        textDate.setText(fragmentAdapter.getDateDisplayed(Calendar.getInstance().getTimeInMillis()));
        textMonth.setText(fragmentAdapter.getMonthDisplayed(pager.getCurrentItem()));

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
        timeLineAdapter = new TimeLineAdapter(getActivity(), timelineArray);
        timelineRecyclerView.setAdapter(timeLineAdapter);

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

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public AppBarLayout getAppBar() {
        return appBarLayout;
    }

    public void getBoard(String title, String text) {
        mMap.get(date).add(new MyCalendar(title, text));
        setEvent();
    }

    public void getMyCalendarContext() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        MyCalendar myCalendar = new MyCalendar();
                        JSONObject jsonResponse = array.getJSONObject(i);
                        myCalendar.setDate(jsonResponse.getString("date"));
                        myCalendar.setText(jsonResponse.getString("text"));
                        myCalendar.setTitle(jsonResponse.getString("title"));
                        myCalendar.setWriter(jsonResponse.getString("writer"));
                        myCalendarAdapter.addItem(myCalendar);
//                        fragmentAdapter.addItem(myCalendar);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/ResidencePosts?residenceName=" + date; // +id
        StringRequest getBoardRequest = new StringRequest(Request.Method.GET, URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getBoardRequest);
    }

    // test
    public void weatherParse() {

//
        Log.d("Tag", "12");
        hour = handler.getHour();
        wfKor = handler.getWfKor();
        temp = handler.getTemp();
        pop = handler.getPop();
        pty = handler.getPty();
        wfText.setText(wfKor);
        tempText.setText(temp);
        popText.setText(pop);
        ptyText.setText(pty);

//        if(pop == "0")
//            linearLayout.setVisibility(View.GONE);
//        else
//            linearLayout.setVisibility(View.VISIBLE);

        switch (wfKor) {
            case "맑음":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db01_b);
                else
                    weatherImage.setImageResource(R.drawable.db01_n_b);
                break;
            case "구름 조금":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db02_b);
                else
                    weatherImage.setImageResource(R.drawable.db02_n_b);
                break;
            case "구름 많음":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db03_b);
                else
                    weatherImage.setImageResource(R.drawable.db03_n_b);
                break;
            case "흐림":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db04_b);
                else
                    weatherImage.setImageResource(R.drawable.db04_n_b);
                break;
            case "비":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db05_b);
                else
                    weatherImage.setImageResource(R.drawable.db05_n_b);
                break;
            case "눈/비":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db06_b);
                else
                    weatherImage.setImageResource(R.drawable.db06_n_b);
                break;
            case "눈":
                if (hour >= 6 && hour < 18)
                    weatherImage.setImageResource(R.drawable.db08_b);
                else
                    weatherImage.setImageResource(R.drawable.db08_n_b);
                break;
        }

        Log.d("Tag", "2");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}