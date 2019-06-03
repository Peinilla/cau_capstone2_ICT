package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.*;

public class MainActivity extends AppCompatActivity {
    private long pressedTime;
    private OnBackPressedListener mBackListener;
    MyPagerAdapter adapter;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        ChatHubManager.getInstance();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.profile);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.home);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
        View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.univ);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
        View view4 = getLayoutInflater().inflate(R.layout.customtab, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.iam);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
        View view5 = getLayoutInflater().inflate(R.layout.customtab, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.calendar);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view5));

//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.pro));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.univ));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.im));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cal));
//        tabLayout.height

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                OnBackManager.getInstance().setPosition(i);
                Object o = OnBackManager.getInstance().getOnBackList();
                setOnBackPressedListener((MainActivity.OnBackPressedListener)o);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        GpsManager.getInstance().setmContext(this);
        GpsManager.getInstance().Update();
        BuildingManager.getInstance().setData(this);

    }

    public interface OnBackPressedListener {
        public void onBack();
    }


    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mBackListener != null) {
            mBackListener.onBack();
        } else {
            if (pressedTime == 0) {
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if (seconds > 2000) {
                    pressedTime = 0;
                } else {
                    super.onBackPressed();
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
    }

    @Override
    public void onPause() {
        //ChatHubManager.getInstance().disconnect();
        super.onPause();
    }

    @Override
    public void finish() {
        ChatHubManager.getInstance().disconnect();
        super.finish();
    }

    @Override
    protected void onResume() {
        ChatHubManager.getInstance().connect();
        super.onResume();
    }
}