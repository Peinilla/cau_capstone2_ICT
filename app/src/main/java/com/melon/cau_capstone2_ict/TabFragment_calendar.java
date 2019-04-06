package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

public class TabFragment_calendar extends Fragment {
    private CalendarView calendarView;
    private ListView listView_timeline, listView_my;
    private TextView textView_date;
    private ArrayAdapter adapter_timeline, adapter_temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_calendar, container, false);
        calendarView = rootView.findViewById(R.id.calendar);
        textView_date = rootView.findViewById(R.id.textview_date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day){
                String date = year + "/" + Integer.toString((int)month + 1) + "/" + day;
                textView_date.setText(date);
            }
        });

        final String[] List_TimeLine = {"리스트01", "리스트02", "리스트03", "리스트04", "리스트05", "리스트06", "리스트07"
                , "리스트08", "리스트09", "리스트10", "리스트11", "리스트12", "리스트13", "리스트14", "리스트15", "리스트16"
                , "리스트17", "리스트18", "리스트19", "리스트20", "리스트21", "리스트22", "리스트23", "리스트24", "리스트25"};
        final String[] List_Temp = {"운영체제", "자료구조", "알고리즘"};
        adapter_timeline = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, List_TimeLine);
        adapter_temp = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, List_Temp);
        listView_timeline = rootView.findViewById(R.id.timeline);
        listView_timeline.setAdapter(adapter_timeline);
        listView_my = rootView.findViewById(R.id.listview_my);
        listView_my.setAdapter(adapter_temp);

        return rootView;
    }
}

