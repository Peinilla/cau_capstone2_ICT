package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.melon.cau_capstone2_ict.Manager.MyBoard;

import java.util.StringTokenizer;

public class TabFragment_boardView extends Fragment implements MainActivity.OnBackPressedListener {

    FrameLayout frameLayout;
    TextView titleView;
    TextView textView;
    TextView writerView;
    TextView dateView;
    View view;
    String writer;

    Button joinButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_boardview, container, false);
        final View test = inflater.inflate(R.layout.myboard_fragment, container, false);

        Bundle bundle = getArguments();
        MyBoard m = (MyBoard) bundle.getSerializable("Board");

        titleView = rootView.findViewById(R.id.board_title);
        textView = rootView.findViewById(R.id.board_text);
        dateView = rootView.findViewById(R.id.board_date);
        writerView = rootView.findViewById(R.id.board_writer);
        joinButton = rootView.findViewById(R.id.button_join);
        view = rootView.findViewById(R.id.viewProfile);
        ImageButton btn = (ImageButton) rootView.findViewById(R.id.board_back);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.board_container);

        if (!m.getType().equals("밥파티"))
            joinButton.setVisibility(View.GONE);
        else
            joinButton.setVisibility(View.VISIBLE);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment childFragment = new TabFragment_chatGroup();
                Bundle bundle = new Bundle(1);
                bundle.putString("GroupID", (String) titleView.getText());
                bundle.putInt("ContainerID",R.id.withbab_container);
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.withbab_container, childFragment).commit();
            }
        });

        titleView.setText(m.getTitle());
        textView.setText(m.getText());
        dateView.setText(m.getDate());
        writer = m.getWriter();
        writerView.setText(writer);

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), profileViewActivity.class);
                intent.putExtra("id", writer);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    void goBack() {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }

    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnBackPressedListener(null);
        goBack();
    }

    @Override
    //                     혹시 Context 로 안되시는분은 Activity 로 바꿔보시기 바랍니다.
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnBackPressedListener(this);
    }
}