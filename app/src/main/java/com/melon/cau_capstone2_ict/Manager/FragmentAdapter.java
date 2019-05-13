package com.melon.cau_capstone2_ict.Manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.melon.cau_capstone2_ict.CalendarFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private CalendarFragment.OnFragmentListener onFragmentListener;
    private HashMap<Integer, CalendarFragment> fragmentMap;
    private ArrayList<Long> listMonthByMillis;
    private int numOfMonth;

    public FragmentAdapter(FragmentManager manager) {
        super(manager);
        clearPrevFragments(manager);
        fragmentMap = new HashMap<Integer, CalendarFragment>();
        listMonthByMillis = new ArrayList<>();
    }

    private void clearPrevFragments(FragmentManager manager) {
        List<Fragment> listFragment = manager.getFragments();

        if (listFragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();

            for (Fragment fragment : listFragment) {
                transaction.remove(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        calendar.set(Calendar.DATE, 1);

        for (int i = numOfMonth; i > 0; i--) {
            calendar.add(Calendar.MONTH, -1);

            listMonthByMillis.add(0, calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    public void addNext() {
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        for (int i = 0; i < numOfMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            listMonthByMillis.add(calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;

        Calendar calendar = Calendar.getInstance();
        //Todo: -
        calendar.add(Calendar.MONTH, numOfMonth);
        calendar.set(Calendar.DATE, 1);

        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH, 1);
        }

        notifyDataSetChanged();
    }

    public void setOnFragmentListener(CalendarFragment.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    @Override
    public Fragment getItem(int position) {
        CalendarFragment fragment = null;

        if (fragmentMap.size() > 0) {
            fragment = fragmentMap.get(position);
        }
        if (fragment == null) {
            fragment = CalendarFragment.newInstance(position);
            fragment.setOnFragmentListener(onFragmentListener);
            fragmentMap.put(position, fragment);
        }
        fragment.setTimeByMillis(listMonthByMillis.get(position));

        return fragment;
    }

    @Override
    public int getCount() {
        return listMonthByMillis.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public String getStrYear(int position){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        date.setTime(listMonthByMillis.get(position));

        return dateFormat.format(date);
    }

    public String getStrMonth(int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        date.setTime(listMonthByMillis.get(position));

        return dateFormat.format(date);
    }

//    public String getStrDay(int position) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
//        Date date = new Date();
//        date.setTime(listMonthByMillis.get(position));
//
//        return dateFormat.format(date);
//    }

    public String getStrMonthOfYear(int position){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        date.setTime(listMonthByMillis.get(position));

        return dateFormat.format(date);
    }

    public String getStrDate(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(millis);

        return dateFormat.format(date);
    }
}
