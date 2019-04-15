package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Tag", "main_1");

        Intent intent;
        intent = getIntent();

        ID = intent.getStringExtra("Member_ID");
        name = intent.getStringExtra("Member_Name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionsMenu)findViewById(R.id.floatingButton);
        mViewPager = (ViewPager) findViewById(R.id.container);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("1"));
        tabLayout.addTab(tabLayout.newTab().setText("2"));
        tabLayout.addTab(tabLayout.newTab().setText("3"));
        tabLayout.addTab(tabLayout.newTab().setText("4"));

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
}
