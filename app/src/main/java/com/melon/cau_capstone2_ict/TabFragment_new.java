package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class TabFragment_new extends Fragment {

    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_2, container, false);

        TextView titleView = (TextView)rootView.findViewById(R.id.gps_title);
        ImageButton btn = (ImageButton)rootView.findViewById(R.id.imageButton);
        frameLayout = rootView.findViewById(R.id.gps_board_container);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment childFragment = new GpsBoardFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("boardID", "GPS게시판_01");
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.gps_board_container, childFragment).commit();
                frameLayout.setVisibility(View.VISIBLE);
            }
            });


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


}