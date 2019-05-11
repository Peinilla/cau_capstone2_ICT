package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.melon.cau_capstone2_ict.*;


import java.util.ArrayList;

public class MyBoardAdapter extends BaseAdapter {
    private ArrayList<MyBoard> listViewItemList = new ArrayList<>();

    public MyBoardAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
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
        TextView writerView = (TextView) convertView.findViewById(R.id.chat_writer) ;
        TextView commentView = (TextView) convertView.findViewById(R.id.comment) ;
        TextView recommendView = (TextView) convertView.findViewById(R.id.recommend) ;
        TextView dateView = (TextView) convertView.findViewById(R.id.date) ;

        MyBoard myBoard_item = listViewItemList.get(position);

        if(pos%2 == 0){
            convertView.setBackgroundResource(R.color.color_board_even);
        }

        titleView.setText(myBoard_item.getTitle());
        writerView.setText(myBoard_item.getWriter());
        commentView.setText("("+myBoard_item.getNumComment()+")");
        recommendView.setText("("+myBoard_item.getNumRecommend()+")");
        dateView.setText(myBoard_item.getDate());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //Todo 아이템 추가
    public void addItem(MyBoard m) {
        listViewItemList.add(m);
    }
    public void clear(){
        listViewItemList.clear();
    }
}

