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
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview_myboard, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.title) ;
        TextView writerView = (TextView) convertView.findViewById(R.id.writer) ;
        TextView commentView = (TextView) convertView.findViewById(R.id.comment) ;
        TextView recommendView = (TextView) convertView.findViewById(R.id.recommend) ;
        TextView dateView = (TextView) convertView.findViewById(R.id.date) ;

        MyBoard myBoard_item = listViewItemList.get(position);

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

