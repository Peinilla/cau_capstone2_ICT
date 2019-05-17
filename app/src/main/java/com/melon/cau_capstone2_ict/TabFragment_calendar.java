package com.melon.cau_capstone2_ict;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.CalendarRequest;
import com.melon.cau_capstone2_ict.Manager.DBHelper;
import com.melon.cau_capstone2_ict.Manager.FragmentAdapter;
import com.melon.cau_capstone2_ict.Manager.MyCalendar;
import com.melon.cau_capstone2_ict.Manager.MyCalendarAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
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
import java.util.List;
import java.util.Map;

public class TabFragment_calendar extends Fragment implements CalendarFragment.OnFragmentListener {
    private String userId;
    private String date;
    private String year, month, day;

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
    private TextView textMonthOfYear;
    private FragmentAdapter fragmentAdapter;

    // Test
    private FloatingActionButton fab_add;
    private FloatingActionButton fab_reload;
    private FrameLayout frameLayout;
    private AppBarLayout appBarLayout;

    // test
    private DBHelper dbHelper;

    // weather
    ImageView weatherImage;
    TextView tempText;
    TextView popText;
    TextView ptyText;
    TextView wfText;
    LinearLayout linearLayout;
    private String finalUrl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159060500";
    private RSSHandler handler = new RSSHandler(finalUrl);
    String temp, wfKor, pty, pop;
    int hour;

    String[] dateList = {"선택안함", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        mSwitch = (Switch) rootView.findViewById(R.id.calendar_switch);
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_my);
        timelineRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_timeline);
        pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);
        textDate = (TextView) rootView.findViewById(R.id.text_calendar_date);
        textMonthOfYear = (TextView) rootView.findViewById(R.id.text_calendar_title);

        // test
        fab_add = (FloatingActionButton) rootView.findViewById(R.id.button_calendar_add);
        fab_reload = (FloatingActionButton) rootView.findViewById(R.id.button_calendar_reload);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.calendar_board_container);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.calendar_appbar);

        // weather
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
                addCalendar();
                refreshFragment();
            }
        });

        fab_reload.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo reload
                refreshFragment();
                setEvent();
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
                year = fragmentAdapter.getStrYear(position);
                month = fragmentAdapter.getStrMonth(position);
                textMonthOfYear.setText(fragmentAdapter.getStrMonthOfYear(position));
                getTimeLineContext();
                setEvent();

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
                refreshFragment();
                if (!isChecked) {
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

    public void addCalendar() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText titleText = new EditText(getContext());
        titleText.setHint("제목");
        final EditText contentText = new EditText(getContext());
        contentText.setHint("내용");
        layout.addView(titleText);
        layout.addView(contentText);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(date).setView(layout).setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String writer = MyUserData.getInstance().getNickname();
                String title = titleText.getText().toString();
                String content = contentText.getText().toString();
                if (dbHelper == null) {
                    dbHelper = new DBHelper(getContext(), "calendar", null, 1);
                }
                MyCalendar myCalendar = new MyCalendar();
                myCalendar.setWriter(writer);
                myCalendar.setTitle(title);
                myCalendar.setContent(content);
                myCalendar.setDate(date);
                dbHelper.add(myCalendar);
                writeRequest(myCalendar);
                refreshFragment();
                setEvent();
            }
        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }

    public void setEvent() {

        Log.d("Tag", "2");
        // Todo 월별로
        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), "calendar", null, 1);
        }
        ArrayList<String> myCalendars = dbHelper.getCalendarByMonth(year, month);

        if (!myCalendars.isEmpty() && pager.getChildCount() != 0) {
            for (int i = 0; i < pager.getChildCount(); i++) {
                if (((CalendarView) pager.getChildAt(i)).getChildCount() != 0) {
                    for (int j = 7; j < ((CalendarView) pager.getChildAt(i)).getChildCount(); j++) {
                        if (myCalendars.size() == 0)
                            break;
                        CalendarItemView child = ((CalendarItemView) ((CalendarView) pager.getChildAt(i)).getChildAt(j));

                        String childDate = fragmentAdapter.getStrDate(child.getDate());
                        if (myCalendars.contains(childDate)) {
                            child.setIsEvent(true);
                            child.invalidate();
                            myCalendars.remove(childDate);
                        } else {
                            if (child.getIsEvent())
                                child.setIsEvent(false);
                        }
                    }
                }
            }
        }
        textDate.setText(date);
    }

    public void initList() {
        userId = MyUserData.getInstance().getId();
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        fragmentAdapter.setOnFragmentListener(this);
        fragmentAdapter.setNumOfMonth(COUNT_PAGE);
        pager.setAdapter(fragmentAdapter);
        pager.setCurrentItem(COUNT_PAGE);

        //Todo year, month, day로 만들기
        year = fragmentAdapter.getStrYear(pager.getCurrentItem());
        month = fragmentAdapter.getStrMonth(pager.getCurrentItem());

        textMonthOfYear.setText(fragmentAdapter.getStrMonthOfYear(pager.getCurrentItem()));
        date = fragmentAdapter.getStrDate(Calendar.getInstance().getTimeInMillis());
        textDate.setText(date);

        dbHelper = new DBHelper(getContext(), "calendar", null, 1);
        dbHelper.calendarDB();

        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        timelineLayoutManager = new LinearLayoutManager(getActivity());
        timelineRecyclerView.setLayoutManager(timelineLayoutManager);

        List myCalendars = dbHelper.getCalendarByDate(date);
        myRecyclerView.setAdapter(new MyCalendarAdapter(getActivity(), myCalendars));

        getTimeLineContext();

        timelineRecyclerView.setVisibility(View.GONE);
    }

    public void refreshFragment() {
        date = textDate.getText().toString();

        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), "calendar", null, 1);
        }
        List myCalendars = dbHelper.getCalendarByDate(date);
        myRecyclerView.setAdapter(new MyCalendarAdapter(getActivity(), myCalendars));
        getTimeLineContext();

        if (!mSwitch.isChecked()) {
            myRecyclerView.setVisibility(View.VISIBLE);
            timelineRecyclerView.setVisibility(View.GONE);
        } else {
            myRecyclerView.setVisibility(View.GONE);
            timelineRecyclerView.setVisibility(View.VISIBLE);
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

    void writeRequest(MyCalendar myCalendar) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        CalendarRequest cr = new CalendarRequest(myCalendar, "delete", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cr);
    }

    public void getTimeLineContext() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        TimeLine timeLine = new TimeLine();
                        JSONObject jsonResponse = array.getJSONObject(i);
                        timeLine.setWriter(jsonResponse.getString("writer"));
                        timeLine.setTitle(jsonResponse.getString("title"));
                        timeLine.setContent(jsonResponse.getString("content"));
                        timeLine.setDate(jsonResponse.getString("date"));
                        timeLineAdapter.addItem(timeLine);
                    }
                    timelineRecyclerView.setAdapter(timeLineAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/" + userId + "/calendar/" + year + "/" + month;
        StringRequest getCalendarRequest = new StringRequest(Request.Method.GET, URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getCalendarRequest);
    }

    // weather
    public void weatherParse() {
        hour = handler.getHour();
        wfKor = handler.getWfKor();
        temp = handler.getTemp();
        pop = handler.getPop();
        pty = handler.getPty();
        wfText.setText(wfKor);
        tempText.setText(temp);
        popText.setText(pop);
        ptyText.setText(pty);

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
}