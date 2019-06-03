package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.melon.cau_capstone2_ict.Manager.BuildingManager;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.GpsManager;
import com.melon.cau_capstone2_ict.Manager.MyPagerAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
import com.melon.cau_capstone2_ict.Manager.OnBackManager;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

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

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        final View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
        final View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
        final  View view4 = getLayoutInflater().inflate(R.layout.customtab, null);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
        final View view5 = getLayoutInflater().inflate(R.layout.customtab, null);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view5));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.getTabAt(0).getCustomView().setBackgroundResource(R.drawable.profile);
        tabLayout.getTabAt(1).getCustomView().setBackgroundResource(R.drawable.home_no);
        tabLayout.getTabAt(2).getCustomView().setBackgroundResource(R.drawable.univ_no);
        tabLayout.getTabAt(3).getCustomView().setBackgroundResource(R.drawable.iam_no);
        tabLayout.getTabAt(4).getCustomView().setBackgroundResource(R.drawable.calendar_no);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter
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
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).getCustomView().setBackgroundResource(R.drawable.profile);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).getCustomView().setBackgroundResource(R.drawable.home);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).getCustomView().setBackgroundResource(R.drawable.univ);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).getCustomView().setBackgroundResource(R.drawable.iam);
                        break;
                    case 4:
                        tabLayout.getTabAt(4).getCustomView().setBackgroundResource(R.drawable.calendar);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).getCustomView().setBackgroundResource(R.drawable.profile_no);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).getCustomView().setBackgroundResource(R.drawable.home_no);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).getCustomView().setBackgroundResource(R.drawable.univ_no);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).getCustomView().setBackgroundResource(R.drawable.iam_no);
                        break;
                    case 4:
                        tabLayout.getTabAt(4).getCustomView().setBackgroundResource(R.drawable.calendar_no);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        GpsManager.getInstance().setmContext(this);
        GpsManager.getInstance().Update();
        BuildingManager.getInstance().setData(this);

        ChatHubManager.getInstance().connect();
        ChatHubManager.getInstance().getHubProxy_bab().invoke("GetMyBopPartyID", MyUserData.getInstance().getId());
        ChatHubManager.getInstance().getHubProxy_bab().on("onMyBopParty", new SubscriptionHandler1<String>() {
                @Override
                public void run(final String s) {
                Log.d("Tag","기존 밥파티 ID : " +s );
                MyUserData.getInstance().setBop(s);
                if(!s.equals("-1")){
                    MyUserData.getInstance().setBabPartyPosting(true);
                }
                ChatHubManager.getInstance().getHubProxy_bab().removeSubscription("onMyBopParty".toLowerCase());
            }
        }, String.class);
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
        super.onPause();
    }

    @Override
    public void finish() {
        if(!MyUserData.getInstance().isBabPartyPosting()) {
            ChatHubManager.getInstance().getHubProxy_bab().invoke("LeaveBopParty", MyUserData.getInstance().getId());
        }
        ChatHubManager.getInstance().disconnect();
        super.finish();
    }

    @Override
    protected void onResume() {
        ChatHubManager.getInstance().connect();
        super.onResume();
    }
}