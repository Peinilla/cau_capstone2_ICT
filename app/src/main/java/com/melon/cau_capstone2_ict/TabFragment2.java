package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.melon.cau_capstone2_ict.Manager.BuildingManager;
import com.melon.cau_capstone2_ict.Manager.GpsManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

public class TabFragment2 extends Fragment {

    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_2, container, false);

        GpsManager.getInstance().setmContext(getActivity());
        GpsManager.getInstance().Update();
        BuildingManager.getInstance().setData(getActivity());

        TextView titleView = (TextView)rootView.findViewById(R.id.gps_title);
        ImageButton btn = (ImageButton)rootView.findViewById(R.id.imageButton);
        frameLayout = rootView.findViewById(R.id.gps_board_container);

        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)rootView.findViewById(R.id.gps_map);
        imageView.setImage(ImageSource.resource(R.drawable.map));

        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String str = MyUserData.getInstance().getNearBuilding();
                if(!str.equals("권외")) {
                    Fragment childFragment = new GpsBoardFragment();
                    Bundle bundle = new Bundle(1);
                    bundle.putString("boardID", str);
                    childFragment.setArguments(bundle);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.gps_board_container, childFragment).commit();
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
            });


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


}