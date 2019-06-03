package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyReplyAdapter extends BaseAdapter {
    private static final int VIEW_MY = 0 ;
    private static final int VIEW_YOU = 1 ;

    private ArrayList<MyReply> listViewItemList = new ArrayList<>() ;

    public MyReplyAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

            switch (viewType) {
                case VIEW_MY:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_reply_my, parent, false);
                    break;
                case VIEW_YOU:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_reply_you, parent, false);
                    break;
            }

        MyReply myReply_item = listViewItemList.get(position);
        switch (viewType) {
            case VIEW_MY:
                TextView myTextView = (TextView) convertView.findViewById(R.id.reply_text) ;
                TextView mtDateView = (TextView) convertView.findViewById(R.id.reply_date) ;

                myTextView.setText(myReply_item.getText());
                mtDateView.setText(myReply_item.getTime());
                break;
            case VIEW_YOU:
                TextView youTextView = (TextView) convertView.findViewById(R.id.reply_text) ;
                TextView youDateView = (TextView) convertView.findViewById(R.id.reply_date) ;
                TextView youNameView = (TextView) convertView.findViewById(R.id.reply_name) ;

                youTextView.setText(myReply_item.getText());
                youDateView.setText(myReply_item.getTime());
                youNameView.setText(myReply_item.getName());
                break;
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType() ;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(MyReply m) {
        listViewItemList.add(m);
    }
    public void updateDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowTime = sdf.format(date);

        String[] nowDate = nowTime.split("T");

        for(int inx = 0; inx<listViewItemList.size(); inx++){
            String d = listViewItemList.get(inx).getTime();
            String[] str = d.split("T");
            if(str[0].equals(nowDate[0])){
                listViewItemList.get(inx).setTime(str[1].substring(0,5));
            }else{
                listViewItemList.get(inx).setTime(str[0]);
            }
        }
    }
    public void clear(){
        listViewItemList.clear();
    }
}
