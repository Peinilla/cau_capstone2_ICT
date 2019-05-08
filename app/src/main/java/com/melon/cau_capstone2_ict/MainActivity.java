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

    private String ID;
    private String name;
    private ViewPager mViewPager;
    private FloatingActionsMenu fab;

    private long pressedTime;
    private OnBackPressedListener mBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }

        ChatHubManager.getInstance();

        fab = (FloatingActionsMenu)findViewById(R.id.floatingButton);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("프로필"));
        tabLayout.addTab(tabLayout.newTab().setText("거주지"));
        tabLayout.addTab(tabLayout.newTab().setText("현재위치"));
        tabLayout.addTab(tabLayout.newTab().setText("지금나는"));
        tabLayout.addTab(tabLayout.newTab().setText("캘린더"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
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

    }

    public void onClickProfile(View v){
        Intent intent = new Intent(this,ProfileActvity.class);
        startActivity(intent);
    }

    public void replaceFragment(int fragmentId){
        //화면에 보여지는 fragment를 추가하거나 바꿀 수 있는 객체를 만든다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        MyBoardFragment f = new MyBoardFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("boardID", "프래그먼트 변경");
        f.setArguments(bundle);

        if( fragmentId == 1 ) {
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
        if(mBackListener != null) {
            mBackListener.onBack();
            // 리스너가 설정되지 않은 상태(예를들어 메인Fragment)라면
            // 뒤로가기 버튼을 연속적으로 두번 눌렀을 때 앱이 종료됩니다.
        } else {
            Log.e("!!!", "Listener is null");
            if ( pressedTime == 0 ) {
                pressedTime = System.currentTimeMillis();
            }
            else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if ( seconds > 2000 ) {
                    pressedTime = 0 ;
                }
                else {
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
