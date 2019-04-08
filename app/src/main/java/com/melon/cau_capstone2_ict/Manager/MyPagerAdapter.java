package com.melon.cau_capstone2_ict.Manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.melon.cau_capstone2_ict.MyBoardFragment;
import com.melon.cau_capstone2_ict.TabFragment2;
import com.melon.cau_capstone2_ict.TabFragment3;
import com.melon.cau_capstone2_ict.TabFragment_calendar;
import com.melon.cau_capstone2_ict.TabFragment_chat;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyBoardFragment tab1 = new MyBoardFragment();
                //게시판 ID 보내기
                Bundle bundle = new Bundle(1);
                bundle.putString("boardID", "test12345");
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            case 3:
                TabFragment_calendar tab4 = new TabFragment_calendar();
                return tab4;
            case 4:
                TabFragment_chat tab_c = new TabFragment_chat();
                return tab_c;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}