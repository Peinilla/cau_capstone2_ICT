package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melon.cau_capstone2_ict.widget.CalendarItemView;
import com.melon.cau_capstone2_ict.widget.CalendarView;

import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private int position;
    CalendarView calendarView;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View rootView;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        void onFragmentListener(View view);
    }

    public static CalendarFragment newInstance(int position) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("poisition");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar, null);
        initView();
        return rootView;
    }

    protected void initView() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendarView = (CalendarView) rootView.findViewById(R.id.calendarview);
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);

        for (int i = 0; i < maxDateOfMonth + 7; i++) {
            CalendarItemView child = new CalendarItemView(getActivity());
            child.setDate(calendar.getTimeInMillis());

            if (i < 7) {
                child.setStaticText(i);
            } else {
                calendar.add(Calendar.DATE, 1);
            }

            calendarView.addView(child);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && onFragmentListener != null && rootView != null) {
            onFragmentListener.onFragmentListener(rootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    onFragmentListener.onFragmentListener(rootView);
                }
            });
        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }
}
