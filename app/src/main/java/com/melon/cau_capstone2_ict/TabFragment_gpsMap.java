package com.melon.cau_capstone2_ict;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.melon.cau_capstone2_ict.Manager.BuildingManager;
import com.melon.cau_capstone2_ict.Manager.GpsManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import pl.polidea.view.ZoomView;

public class TabFragment_gpsMap extends Fragment {

    FrameLayout frameLayout;
    ImageButton marker;
    ImageButton marker2;
    RelativeLayout c;
    View v;
    int width;
    String currentBuilding;
    String prevBuilding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_gpsmap, container, false);

        GpsManager.getInstance().setmContext(getActivity());
        GpsManager.getInstance().Update();
        BuildingManager.getInstance().setData(getActivity());

        TextView titleView = (TextView)rootView.findViewById(R.id.gps_title);
        ImageButton btn_currentGps = (ImageButton)rootView.findViewById(R.id.imageButton);
        frameLayout = rootView.findViewById(R.id.gps_board_container);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        v = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_item, null, false);
        marker = v.findViewById(R.id.marker1);
        marker2 = v.findViewById(R.id.marker2);
        c = (RelativeLayout) rootView.findViewById(R.id.container);


        setMarker();

        ZoomView zoomView = new ZoomView(getActivity());
        zoomView.addView(v);
        zoomView.setMaxZoom(3f); //배율 설정

        c.addView(zoomView);

        marker.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!currentBuilding.equals("권외")) {
                    //현재 접속 게시판 변경
                    MyUserData.getInstance().setPrevBuilding(currentBuilding);
                    Fragment childFragment = new MyBoardFragment();
                    Bundle bundle = new Bundle(2);
                    bundle.putString("boardID", currentBuilding);
                    bundle.putBoolean("isGPS",true);
                    childFragment.setArguments(bundle);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.gps_board_container, childFragment).commit();
                    frameLayout.setVisibility(View.VISIBLE);

                    setMarker();
                }
            }
            });

        marker2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!prevBuilding.equals("")) {
                    Fragment childFragment = new MyBoardFragment();
                    Bundle bundle = new Bundle(2);
                    bundle.putString("boardID", prevBuilding);
                    bundle.putBoolean("isGPS",true);
                    childFragment.setArguments(bundle);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.gps_board_container, childFragment).commit();
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_currentGps.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                setMarker();
            }
        });
        return rootView;
    }

    void setMarker(){
        final ImageView zoomImage = v.findViewById(R.id.zoom_image);


        currentBuilding = MyUserData.getInstance().getCurrentBuilding();
        prevBuilding = MyUserData.getInstance().getPrevBuilding();

        marker.setVisibility(View.GONE);
        marker2.setVisibility(View.GONE);

        if(!currentBuilding.equals("권외") && !currentBuilding.equals("")){
            float m = width / 516;

            if(currentBuilding.equals(prevBuilding)) {
                marker2.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) marker2.getLayoutParams();
                int[] scale = BuildingManager.getInstance().getXY(prevBuilding);
                params.topMargin = (int) (scale[1] * m);
                params.leftMargin = (int) (scale[0] * m);
                marker2.setLayoutParams(params);
            }else {
                marker.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) marker.getLayoutParams();
                int[] scale = BuildingManager.getInstance().getXY(currentBuilding);
                params.topMargin = (int) (scale[1] * m);
                params.leftMargin = (int) (scale[0] * m);
                marker.setLayoutParams(params);

                if(!prevBuilding.equals("")){
                    marker2.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams) marker2.getLayoutParams();
                    scale = BuildingManager.getInstance().getXY(prevBuilding);
                    params.topMargin = (int) (scale[1] * m);
                    params.leftMargin = (int) (scale[0] * m);
                    marker2.setLayoutParams(params);
                }
            }
        }else{

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }



}