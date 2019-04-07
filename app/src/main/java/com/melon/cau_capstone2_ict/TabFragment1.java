package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.MyBoardAdapter;

public class TabFragment1 extends Fragment {

    MyBoardAdapter adapter;
    ListView listView;
    BottomNavigationView bnv;
    FloatingActionButton searchButton;
    ImageButton searchImage;
    ImageButton closeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);

        bnv = (BottomNavigationView)rootView.findViewById(R.id.SearchBar) ;
        listView = (ListView) rootView.findViewById(R.id.listview1);
        searchButton = (FloatingActionButton) rootView.findViewById(R.id.button_search);
        searchImage = (ImageButton) rootView.findViewById(R.id.image_search);
        closeImage = (ImageButton) rootView.findViewById(R.id.image_close);

        adapter = new MyBoardAdapter();

        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();



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

}