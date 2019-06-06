package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.util.ArrayList;

public class MyNowimAdapter extends BaseAdapter {
    private ArrayList<MyNowim> listViewItemList = new ArrayList<>();

    public MyNowimAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview_mynowim, parent, false);
        }

        TextView wordView = (TextView) convertView.findViewById(R.id.now_word) ;
        TextView countView = (TextView) convertView.findViewById(R.id.now_count) ;

        MyNowim item = listViewItemList.get(position);

        wordView.setText(item.getWord());
        countView.setText(String.valueOf(item.getCount()));

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
    public void addItem(MyNowim m) {
        listViewItemList.add(m);
    }
    public void addItem(String word,int count) {
        MyNowim m = new MyNowim(word,count);
        listViewItemList.add(m);
    }
    public void clear(){
        listViewItemList.clear();
    }
}

