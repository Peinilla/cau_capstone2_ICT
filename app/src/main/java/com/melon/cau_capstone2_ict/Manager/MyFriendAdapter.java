package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.util.ArrayList;

public class MyFriendAdapter extends BaseAdapter {

    private ArrayList<MyFriendinfo> listViewItemList = new ArrayList<>() ;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview_myfriend, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.friend_name) ;
        ImageView nfcView = convertView.findViewById(R.id.friend_nfc);

        MyFriendinfo m = listViewItemList.get(position);

        nameView.setText(m.getNickname());
        if(m.getType() == 2){
            nfcView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }
    public void addItem(String name,String id,int type) {
        MyFriendinfo m =  new MyFriendinfo(name, id , type);
        listViewItemList.add(m);
    }

    public void clear(){
        listViewItemList.clear();
    }

}
