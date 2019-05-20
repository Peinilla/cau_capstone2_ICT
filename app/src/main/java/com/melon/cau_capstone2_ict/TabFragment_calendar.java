package com.melon.cau_capstone2_ict;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.util.StringTokenizer;

public class TabFragment_calendar extends Fragment implements CalendarFragment.OnFragmentListener {
    private String userId;
    private String date;
    private String year, month;
    boolean init = false;

    private Switch mSwitch;

    private RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private RecyclerView timelineRecyclerView;
    private LinearLayoutManager timelineLayoutManager;

    private static final int COUNT_PAGE = 12;
    private ViewPager pager;
    private TextView textDate;
    private TextView textMonthOfYear;
    private FragmentAdapter fragmentAdapter;

    private FloatingActionButton fab_add;
    private FloatingActionButton fab_reload;
    private FrameLayout frameLayout;
    private AppBarLayout appBarLayout;

    private DBHelper dbHelper;

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

    String[] timeList = {"종일", "00시", "01시", "02시", "03시", "04시", "05시", "06시", "07시", "08시", "09시", "10시", "11시", "12시",
            "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시", "24시"};
    String[] colorList = {"black", "gray", "white", "blue", "l_blue", "green", "l_green", "red", "pink", "orange", "yellow", "purple"};
    String str1 = "", str2 = "", color = "";
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);

        mSwitch = (Switch) rootView.findViewById(R.id.calendar_switch);
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_my);
        timelineRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_timeline);
        pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);
        textDate = (TextView) rootView.findViewById(R.id.text_calendar_date);
        textMonthOfYear = (TextView) rootView.findViewById(R.id.text_calendar_title);

        fab_add = (FloatingActionButton) rootView.findViewById(R.id.button_calendar_add);
        fab_reload = (FloatingActionButton) rootView.findViewById(R.id.button_calendar_reload);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.calendar_board_container);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.calendar_appbar);

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
                refreshSelectDate();
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
        final LinearLayout layoutTime = new LinearLayout(getContext());
        layoutTime.setOrientation(LinearLayout.HORIZONTAL);
        final TextView timeText = new TextView(getContext());
        timeText.setText("시간: ");
        final Spinner spinnerStart = new Spinner(getContext());
        final ArrayAdapter<String> spAdapterStartDate = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, timeList);
        spinnerStart.setAdapter(spAdapterStartDate);
        final TextView forText = new TextView(getContext());
        forText.setText("-");
        forText.setPadding(5, 0, 5, 0);
        final Spinner spinnerEnd = new Spinner(getContext());
        final ArrayAdapter<String> spAdapterEndDate = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, timeList);
        spinnerEnd.setAdapter(spAdapterEndDate);
        final Spinner spinnerColor = new Spinner(getContext());
        spinnerColor.setAdapter(new MyCustomAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, colorList));
        layoutTime.addView(timeText);
        layoutTime.addView(spinnerStart);
        layoutTime.addView(forText);
        layoutTime.addView(spinnerEnd);
        layoutTime.addView(spinnerColor);
        final EditText titleText = new EditText(getContext());
        titleText.setHint("제목");
        final EditText contentText = new EditText(getContext());
        contentText.setHint("내용");
        layout.addView(layoutTime);
        layout.addView(titleText);
        layout.addView(contentText);

        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                str1 = (String) parent.getItemAtPosition(position);
                if (str1.equals("종일")) {
                    spinnerEnd.setEnabled(false);
                    str1 = "";
                    str2 = "";
                } else {
                    spinnerEnd.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(pos > position && position != 0){
                    parent.setSelection(pos);
                }
                str2 = (String) parent.getItemAtPosition(position);
                if (str2.equals("종일")) {
                    str2 = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(date).setView(layout).setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String writer = MyUserData.getInstance().getNickname();
                String title = color + "|";
                if (!str1.equals("")) {
                    title += str1 + " - ";
                    if (!str2.equals(""))
                        title += str2;
                }
                else{
                    title += "하루 종일";
                }
                title += "|";
                title += titleText.getText().toString();
                String content = contentText.getText().toString();
                if (dbHelper == null) {
                    dbHelper = new DBHelper(getContext(), MyUserData.getInstance().getNickname(), null, 1);
                }
                MyCalendar myCalendar = new MyCalendar();
                myCalendar.setWriter(writer);
                myCalendar.setTitle(title);
                myCalendar.setContent(content);
                myCalendar.setDate(date);
                writeRequest(myCalendar);
                StringTokenizer tokens = new StringTokenizer(title, "|");
                String token = tokens.nextToken();
                myCalendar.setColor(token);
                token = tokens.nextToken();
                myCalendar.setTime(token);
                token = tokens.nextToken();
                for(; tokens.hasMoreElements();)
                    token += "|" + tokens.nextToken();
                myCalendar.setTitle(token);
                dbHelper.add(myCalendar);
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
        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), MyUserData.getInstance().getNickname(), null, 1);
        }

        if (pager.getChildCount() != 0) {
            for (int i = 0; i < pager.getChildCount(); i++) {
                if (((CalendarView) pager.getChildAt(i)).getChildCount() != 0) {
                    for (int j = 7; j < ((CalendarView) pager.getChildAt(i)).getChildCount(); j++) {
                        CalendarItemView child = ((CalendarItemView) ((CalendarView) pager.getChildAt(i)).getChildAt(j));
                        String childDate = fragmentAdapter.getStrDate(child.getDate());
                        ArrayList<String> colors = dbHelper.getCalendarByColor(childDate);
                        child.setColor(colors);
                        child.invalidate();
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

        year = fragmentAdapter.getStrYear(pager.getCurrentItem());
        month = fragmentAdapter.getStrMonth(pager.getCurrentItem());

        textMonthOfYear.setText(fragmentAdapter.getStrMonthOfYear(pager.getCurrentItem()));
        date = fragmentAdapter.getStrDate(Calendar.getInstance().getTimeInMillis());
        textDate.setText(date);

        dbHelper = new DBHelper(getContext(), MyUserData.getInstance().getNickname(), null, 1);
        dbHelper.calendarDB();

        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        timelineLayoutManager = new LinearLayoutManager(getActivity());
        timelineRecyclerView.setLayoutManager(timelineLayoutManager);

        getMyCalendarContext();
        getTimeLineContext();

        timelineRecyclerView.setVisibility(View.GONE);
    }

    public void refreshFragment() {
        date = textDate.getText().toString();

        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), MyUserData.getInstance().getNickname(), null, 1);
        }
        ArrayList<MyCalendar> myCalendars = dbHelper.getCalendarByDate(date);
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

    public void refreshSelectDate() {
        date = textDate.getText().toString();

        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), MyUserData.getInstance().getNickname(), null, 1);
        }
        ArrayList<MyCalendar> myCalendars = dbHelper.getCalendarByDate(date);
        myRecyclerView.setAdapter(new MyCalendarAdapter(getActivity(), myCalendars));

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
        CalendarRequest cr = new CalendarRequest(myCalendar, "new", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cr);
    }

    public void getMyCalendarContext() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<MyCalendar> myCalendars = new ArrayList<>();
                    String res = response.substring(1,response.length()-1);
                    res = res.replace("\\","");
                    Log.d("Tag", "response check : " + res);
                    JSONArray array = new JSONArray(res);
                    
                    for (int inx = 0; inx < array.length(); inx++) {
                        MyCalendar myCalendar = new MyCalendar();
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        myCalendar.setWriter(MyUserData.getInstance().getNickname());
                        StringTokenizer tokens = new StringTokenizer(jsonResponse.getString("title"), "|");
                        String token = tokens.nextToken();
                        myCalendar.setColor(token);
                        token = tokens.nextToken();
                        myCalendar.setTime(token);
                        token = tokens.nextToken();
                        for (; tokens.hasMoreElements(); )
                            token += "|" + tokens.nextToken();
                        myCalendar.setTitle(token);
                        myCalendar.setContent(jsonResponse.getString("content"));
                        String date = jsonResponse.get("date").toString().substring(0, 10);
                        myCalendar.setDate(date);
                        myCalendars.add(myCalendar);
                    }
                    dbHelper.addList(myCalendars);
                    myRecyclerView.setAdapter(new MyCalendarAdapter(getActivity(), dbHelper.getAllMyCalendars()));
                    setEvent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/" + userId + "/calender";
        StringRequest getCalendarRequest = new StringRequest(Request.Method.GET, URL, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getCalendarRequest);
    }

    public void getTimeLineContext() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<TimeLine> timeLines = new ArrayList<>();
                    String res = response.substring(1,response.length()-1);
                    res = res.replace("\\","");
                    Log.d("Tag", "response check : " + res);
                    JSONArray array = new JSONArray(res);
                    for (int inx = 0; inx < array.length(); inx++) {
                        TimeLine timeLine = new TimeLine();
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        timeLine.setWriter(jsonResponse.getString("nickname"));
                        StringTokenizer tokens = new StringTokenizer(jsonResponse.getString("title"), "|");
                        String token = tokens.nextToken();
                        timeLine.setColor(token);
                        token = tokens.nextToken();
                        timeLine.setTime(token);
                        token = tokens.nextToken();
                        for (; tokens.hasMoreElements(); )
                            token += "|" + tokens.nextToken();
                        timeLine.setTitle(token);
                        timeLine.setContent(jsonResponse.getString("content"));
                        String date = jsonResponse.get("date").toString().substring(0, 10);
                        timeLine.setDate(date);
                        timeLines.add(timeLine);
                    }
                    timelineRecyclerView.setAdapter(new TimeLineAdapter(getActivity(), timeLines));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/" + userId + "/calender/" + year + "/" + month;
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
        tempText.setText(temp + "℃");
        popText.setText(pop + "%");
        ptyText.setText(pty + "mm");

        if (pop.equals("0")) {
            linearLayout.setVisibility(View.GONE);
        } else
            linearLayout.setVisibility(View.VISIBLE);

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
    }

    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.color_row, parent, false);
            ImageView label = (ImageView) row.findViewById(R.id.image_color);

            switch (position) {
                case 0:
                    label.setImageResource(R.drawable.black);
                    break;
                case 1:
                    label.setImageResource(R.drawable.gray);
                    break;
                case 2:
                    label.setImageResource(R.drawable.white);
                    break;
                case 3:
                    label.setImageResource(R.drawable.blue);
                    break;
                case 4:
                    label.setImageResource(R.drawable.l_blue);
                    break;
                case 5:
                    label.setImageResource(R.drawable.green);
                    break;
                case 6:
                    label.setImageResource(R.drawable.l_green);
                    break;
                case 7:
                    label.setImageResource(R.drawable.red);
                    break;
                case 8:
                    label.setImageResource(R.drawable.pink);
                    break;
                case 9:
                    label.setImageResource(R.drawable.orange);
                    break;
                case 10:
                    label.setImageResource(R.drawable.yellow);
                    break;
                case 11:
                    label.setImageResource(R.drawable.purple);
                    break;
            }
            return row;
        }
    }
}