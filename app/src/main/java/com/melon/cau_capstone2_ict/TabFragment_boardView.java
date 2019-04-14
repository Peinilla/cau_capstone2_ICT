package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.MyBoard;

public class TabFragment_boardView extends Fragment implements MainActivity.OnBackPressedListener{

    FrameLayout frameLayout;
    TextView titleView;
    TextView textView;
    TextView dateView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_boardview, container, false);
        final View test = inflater.inflate(R.layout.myboard_fragment, container, false);

        Log.d("Tag", "check");

        Bundle bundle = getArguments();
        MyBoard m = (MyBoard) bundle.getSerializable("Board");
        Log.d("Tag", "check");

        titleView = rootView.findViewById(R.id.board_title);
        textView = rootView.findViewById(R.id.board_text);
        dateView = rootView.findViewById(R.id.board_date);
        Log.d("Tag", "check" + m.getTitle());
        Log.d("Tag", "check" + m.getText());
        Log.d("Tag", "check" + m.getDate());

        titleView.setText(m.getTitle());
        textView.setText(m.getText());
        dateView.setText(m.getDate());
        Log.d("Tag", "check");

        ImageButton btn = (ImageButton)rootView.findViewById(R.id.board_back);

        frameLayout = (FrameLayout)rootView.findViewById(R.id.board_container);

        Log.d("Tag", "check");


        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
            });


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }

    @Override
    public void onBack() {
        Log.e("Tag", "onBack()");
        // 리스너를 설정하기 위해 Activity 를 받아옵니다.
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);
        // MainFragment 로 교체
        goBack();


    }
    @Override
    //                     혹시 Context 로 안되시는분은 Activity 로 바꿔보시기 바랍니다.
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }



}