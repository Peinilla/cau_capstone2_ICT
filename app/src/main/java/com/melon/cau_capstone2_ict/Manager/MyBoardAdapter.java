package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.melon.cau_capstone2_ict.*;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;

public class MyBoardAdapter extends BaseAdapter {
    private ArrayList<MyBoard> listViewItemList = new ArrayList<>();
    private ArrayList<MyBoard> cloneList = new ArrayList<>();

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
        ImageButton riceView = (ImageButton) convertView.findViewById(R.id.image_rice);

        MyBoard myBoard_item = listViewItemList.get(position);

        if(pos%2 == 0){
            convertView.setBackgroundResource(R.color.color_board_even);
        }

        if(myBoard_item.getType().equals("밥파티")){
            riceView.setVisibility(View.VISIBLE);
        }else{
            riceView.setVisibility(View.GONE);
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
    public void updateDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowTime = sdf.format(date);

        String[] nowDate = nowTime.split("T");

        for(int inx = 0; inx<listViewItemList.size(); inx++){
            String d = listViewItemList.get(inx).getDate();
            String[] str = d.split("T");
            if(str[0].equals(nowDate[0])){
                listViewItemList.get(inx).setDate(str[1].substring(0,5));
            }else{
                listViewItemList.get(inx).setDate(str[0]);
            }
        }
    }
    public void clear(){
        cloneList.clear();
        listViewItemList.clear();
    }

    public void reverse() {
        cloneList.addAll(listViewItemList);
        Collections.reverse(listViewItemList);
    }

    public void search(int type, String word){
        listViewItemList.clear();
        listViewItemList.addAll(cloneList);

        for(int inx = 0; inx<listViewItemList.size(); inx++){
            if(type == 0){
                if(!listViewItemList.get(inx).getTitle().contains(word)){
                    listViewItemList.remove(inx);
                    inx --;
                }
            }else{
                if(!listViewItemList.get(inx).getWriter().contains(word)){
                    listViewItemList.remove(inx);
                    inx --;
                }
            }
        }
    }
}

