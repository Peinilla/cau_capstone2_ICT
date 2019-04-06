package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.R.id.*;
import android.widget.ListView;

import com.melon.cau_capstone2_ict.Manager.MyBoardAdapter;

public class TabFragment1 extends Fragment {

    MyBoardAdapter adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview1);

        adapter = new MyBoardAdapter();

        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();
        adapter.addItem();


        listView.setAdapter(adapter);

        return rootView;
    }
}