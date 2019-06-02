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

import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private long pressedTime;
    private OnBackPressedListener mBackListener;

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
//        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.profile);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        final View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.home_no);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
        final View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.univ_no);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
        final  View view4 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.iam_no);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
        final View view5 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.calendar_no);
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
                Log.d("check1", Integer.toString(i));
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("check2", Integer.toString(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d("check3", Integer.toString(i));
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("check4", Integer.toString(tab.getPosition()));
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
                Log.d("check5", Integer.toString(tab.getPosition()));
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
                Log.d("check6", Integer.toString(tab.getPosition()));

            }
        });

    }

    public void onClickProfile(View v) {
        Intent intent = new Intent(this, ProfileActvity.class);
        startActivity(intent);
    }

    public void replaceFragment(int fragmentId) {
        //화면에 보여지는 fragment를 추가하거나 바꿀 수 있는 객체를 만든다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        MyBoardFragment f = new MyBoardFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("boardID", "프래그먼트 변경");
        f.setArguments(bundle);

        if (fragmentId == 1) {
            transaction.replace(R.id.container, f);
        }
        //Back 버튼 클릭 시 이전 프래그먼트로 이동시키도록 한다.

        transaction.addToBackStack(null);
        //fragment의 변경사항을 반영시킨다.

        transaction.commit();
    }

    public interface OnBackPressedListener {
        public void onBack();
    }


    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
        if (mBackListener != null) {
            mBackListener.onBack();
            // 리스너가 설정되지 않은 상태(예를들어 메인Fragment)라면
            // 뒤로가기 버튼을 연속적으로 두번 눌렀을 때 앱이 종료됩니다.
        } else {
            Log.e("!!!", "Listener is null");
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
        ChatHubManager.getInstance().disconnect();
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}