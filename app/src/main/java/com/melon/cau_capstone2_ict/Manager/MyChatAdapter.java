package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyChatAdapter extends BaseAdapter {
    private static final int VIEW_MYCHAT = 0 ;
    private static final int VIEW_MYCHAT_RE = 1 ;
    private static final int VIEW_YOUCHAT = 2 ;
    private static final int VIEW_YOUCHAT_RE = 3 ;
    private static final int VIEW_MAX = 4 ;


    private ArrayList<MyChat> listViewItemList = new ArrayList<>() ;

    public MyChatAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            Log.d("Tag", "pos : " + pos);
            switch (viewType) {
                case VIEW_MYCHAT:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_mychat, parent, false);
                    break;
                case VIEW_MYCHAT_RE:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_mychat, parent, false);
                    break;
                case VIEW_YOUCHAT:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_youchat, parent, false);
                    break;
                case VIEW_YOUCHAT_RE:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_youchatre, parent, false);
                    break;
            }
        }
            MyChat myChat_item = listViewItemList.get(position);
            switch (viewType){
                case VIEW_MYCHAT:
                    TextView myTextView = (TextView) convertView.findViewById(R.id.chat_mytext) ;
                    TextView mtDateView = (TextView) convertView.findViewById(R.id.chat_mydate) ;

                    myTextView.setText(myChat_item.getText());
                    mtDateView.setText(myChat_item.getDate());
                    break;
                case VIEW_MYCHAT_RE:
                    TextView myTextViewRe = (TextView) convertView.findViewById(R.id.chat_mytext) ;
                    TextView mtDateViewRe = (TextView) convertView.findViewById(R.id.chat_mydate) ;

                    myTextViewRe.setText(myChat_item.getText());
                    mtDateViewRe.setText(myChat_item.getDate());
                    break;
                case VIEW_YOUCHAT:
                    TextView youTextView = (TextView) convertView.findViewById(R.id.chat_youtext) ;
                    TextView writerView = (TextView) convertView.findViewById(R.id.chat_writer) ;
                    TextView youDateView = (TextView) convertView.findViewById(R.id.chat_youdate) ;

                    youTextView.setText(myChat_item.getText());
                    writerView.setText(myChat_item.getWriter());
                    youDateView.setText(myChat_item.getDate());
                    break;
                case VIEW_YOUCHAT_RE:
                    TextView youTextViewRe = (TextView) convertView.findViewById(R.id.chat_youtext) ;
                    TextView youDateViewRe = (TextView) convertView.findViewById(R.id.chat_youdate) ;

                    youTextViewRe.setText(myChat_item.getText());
                    youDateViewRe.setText(myChat_item.getDate());
                    break;
            }
        return convertView;
    }
    @Override
    public int getViewTypeCount() {
        return VIEW_MAX;
    }

    // position 위치의 아이템 타입 리턴.
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

    public void addItem(int t,String text, String w) {
        MyChat m = new MyChat(t);
        m.setWriter(w);
        m.setText(text);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowTime = sdf.format(date);
        m.setDate(nowTime.substring(0,nowTime.length()-3));

        int size = listViewItemList.size();
        if(size == 0){
            listViewItemList.add(m);
            return;
        }
        if(listViewItemList.get(size-1).getType()/2 == t/2){
            if(listViewItemList.get(size-1).getDate().equals(m.getDate())){
                listViewItemList.get(size-1).setDate("");
            }
            if(listViewItemList.get(size-1).getWriter().equals(m.getWriter())){
                if(t!=0) {
                    m.setType(VIEW_YOUCHAT_RE);
                }
            }
        }

        listViewItemList.add(m);
    }
    public void addItem(int t,String text, String w, String date) {
        MyChat m = new MyChat(t);
        m.setWriter(w);
        m.setText(text);
        m.setDate(date.substring(0,date.length()-3));

        Log.d("Tag","text : " + text);
        int size = listViewItemList.size();
        if(size == 0){
            listViewItemList.add(m);
            Log.d("Tag","input text : " + m.getText());

            return;
        }
        if(listViewItemList.get(size-1).getType()/2 == t/2) {
            if (listViewItemList.get(size - 1).getDate().equals(m.getDate())) {
                listViewItemList.get(size - 1).setDate("");
            }
            if (listViewItemList.get(size - 1).getWriter().equals(m.getWriter())) {
                if (t != 0) {
                    m.setType(VIEW_YOUCHAT_RE);
                }
            }
        }
        Log.d("Tag","input text : " + m.getText());

        listViewItemList.add(m);
    }
}
