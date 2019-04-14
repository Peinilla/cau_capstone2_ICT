package com.melon.cau_capstone2_ict.Manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.melon.cau_capstone2_ict.MyBoardFragment;
import com.melon.cau_capstone2_ict.TabFragment2;
import com.melon.cau_capstone2_ict.TabFragment_chat;
import com.melon.cau_capstone2_ict.TabFragment_calendar;
import com.melon.cau_capstone2_ict.TabFragment_profile;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                MyBoardFragment tab1 = new MyBoardFragment();
                //게시판 ID 보내기
                Bundle bundle = new Bundle(1);
                bundle.putString("boardID", MyUserData.getInstance().getResidence());
                tab1.setArguments(bundle);
                return tab1;
            case 2:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 3:
                TabFragment_chat tab3 = new TabFragment_chat();
                return tab3;
            case 4:
                TabFragment_calendar tab4 = new TabFragment_calendar();
                return tab4;
            case 0:
                TabFragment_profile tab_c = new TabFragment_profile();
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