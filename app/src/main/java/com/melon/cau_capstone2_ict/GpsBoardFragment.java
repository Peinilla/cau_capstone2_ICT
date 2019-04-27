package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.MyBoardAdapter;

public class GpsBoardFragment extends Fragment implements MainActivity.OnBackPressedListener {
    String boardID;

    MyBoardAdapter adapter;
    ListView listView;
    BottomNavigationView bnv;
    FloatingActionButton searchButton;
    ImageButton searchImage;
    ImageButton closeImage;
    ImageButton backImage;
    TextView titleView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boardID = getArguments().getString("boardID");
        final View rootView = inflater.inflate(R.layout.gpsboard_fragment, container, false);


        titleView = (TextView)rootView.findViewById(R.id.board_title);
        bnv = (BottomNavigationView)rootView.findViewById(R.id.SearchBar) ;
        listView = (ListView) rootView.findViewById(R.id.listview1);
        searchButton = (FloatingActionButton) rootView.findViewById(R.id.button_search);
        searchImage = (ImageButton) rootView.findViewById(R.id.image_search);
        closeImage = (ImageButton) rootView.findViewById(R.id.image_close);
        backImage = (ImageButton) rootView.findViewById(R.id.gpsboard_back);

        titleView.setText(boardID);

        adapter = new MyBoardAdapter();



        listView.setAdapter(adapter);


        searchButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                bnv.setVisibility(View.VISIBLE);    }
        });


        closeImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                bnv.setVisibility(View.GONE);          }
        });
        backImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();            }
        });


        final Spinner spinner_field = (Spinner) rootView.findViewById(R.id.spinner_search);


        String[] str = getResources().getStringArray(R.array.search_item);


        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_field.setAdapter(adapter);


        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Todo 제목/글쓴이 검색 선택 구현해야됨
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return rootView;
    }
    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.gps_board_container);
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