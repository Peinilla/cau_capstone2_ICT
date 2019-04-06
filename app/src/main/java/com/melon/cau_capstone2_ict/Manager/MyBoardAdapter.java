package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.melon.cau_capstone2_ict.*;


import java.util.ArrayList;

public class MyBoardAdapter extends BaseAdapter {
    private ArrayList<MyBoard> listViewItemList = new ArrayList<>() ;
    private LayoutInflater inflater = null;
    private RecyclerView.ViewHolder viewHolder = null;

    public MyBoardAdapter() {
        Log.d("Tag", "MBA");

    }

    @Override
    public int getCount() {

        Log.d("Tag", "getCount");
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Tag", "getView_1");

        final int pos = position;
        final Context context = parent.getContext();
        Log.d("Tag", "getView_2");

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            Log.d("Tag", "null check");
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview_myboard, parent, false);
        }
        Log.d("Tag", "getView_3");

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleView = (TextView) convertView.findViewById(R.id.title) ;
        TextView writerView = (TextView) convertView.findViewById(R.id.writer) ;
        TextView commentView = (TextView) convertView.findViewById(R.id.comment) ;
        TextView recommendView = (TextView) convertView.findViewById(R.id.recommend) ;
        TextView dateView = (TextView) convertView.findViewById(R.id.date) ;
        Log.d("Tag", "getView_4");

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MyBoard myBoard_item = listViewItemList.get(position);
        Log.d("Tag", "getView_5");

        // 아이템 내 각 위젯에 데이터 반영
        titleView.setText(myBoard_item.getTitle());
        writerView.setText(myBoard_item.getWriter());
        commentView.setText("("+myBoard_item.getNumComment()+")");
        recommendView.setText("("+myBoard_item.getNumRecommend()+")");
        dateView.setText(myBoard_item.getDate());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    //Todo 아이템 추가
    public void addItem() {
        MyBoard m = new MyBoard();

        listViewItemList.add(m);
    }
}

