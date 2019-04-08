package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class GpsBoardFragment extends Fragment {

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

        adapter.addItem("gps 게시판 입니다.","작성자1");
        adapter.addItem("1","test");
        adapter.addItem("가나다라마바사아자차타","test");

        Log.d("Tag", "main_1");


        listView.setAdapter(adapter);
        Log.d("Tag", "main_1");

        searchButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                bnv.setVisibility(View.VISIBLE);    }
        });
        Log.d("Tag", "main_1");

        closeImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                bnv.setVisibility(View.GONE);          }
        });
        backImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(GpsBoardFragment.this).commit();
                fragmentManager.popBackStack();
            }
        });


        final Spinner spinner_field = (Spinner) rootView.findViewById(R.id.spinner_search);

        Log.d("Tag", "main_1");

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
        Log.d("Tag", "end");

        return rootView;
    }

}