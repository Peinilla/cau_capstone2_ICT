package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.util.ArrayList;

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


            MyChat myChat_item = listViewItemList.get(position);

            switch (viewType){
                case VIEW_MYCHAT:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_mychat, parent, false);
                    TextView myTextView = (TextView) convertView.findViewById(R.id.chat_mytext) ;
                    TextView mtDateView = (TextView) convertView.findViewById(R.id.chat_mydate) ;

                    myTextView.setText(myChat_item.getText());
                    mtDateView.setText(myChat_item.getDate());
                    break;
                case VIEW_MYCHAT_RE:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_mychat, parent, false);
                    TextView myTextViewRe = (TextView) convertView.findViewById(R.id.chat_mytext) ;
                    TextView mtDateViewRe = (TextView) convertView.findViewById(R.id.chat_mydate) ;

                    myTextViewRe.setText(myChat_item.getText());
                    mtDateViewRe.setText(myChat_item.getDate());
                    break;
                case VIEW_YOUCHAT:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_youchat, parent, false);
                    TextView youTextView = (TextView) convertView.findViewById(R.id.chat_youtext) ;
                    TextView writerView = (TextView) convertView.findViewById(R.id.chat_writer) ;
                    TextView youDateView = (TextView) convertView.findViewById(R.id.chat_youdate) ;

                    youTextView.setText(myChat_item.getText());
                    writerView.setText(myChat_item.getWriter());
                    youDateView.setText(myChat_item.getDate());
                    break;
                case VIEW_YOUCHAT_RE:
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.listview_youchatre, parent, false);
                    TextView youTextViewRe = (TextView) convertView.findViewById(R.id.chat_youtext) ;
                    TextView youDateViewRe = (TextView) convertView.findViewById(R.id.chat_youdate) ;

                    youTextViewRe.setText(myChat_item.getText());
                    youDateViewRe.setText(myChat_item.getDate());
                    break;
            }
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

    //Todo 아이템 추가
    public void addItem(int t,String text, String w) {
        MyChat m = new MyChat(t);
        m.setWriter(w);
        m.setText(text);
        int size = listViewItemList.size();
        if(size == 0){
            listViewItemList.add(m);
            return;
        }
        if(listViewItemList.get(size-1).getType()/2 == t/2){
            if(listViewItemList.get(size-1).getWriter().equals(m.getWriter())){
               m.setType(VIEW_YOUCHAT_RE);
            }
            if(listViewItemList.get(size-1).getDate().equals(m.getDate())){
                listViewItemList.get(size-1).setDate("");
            }
        }
        listViewItemList.add(m);
    }
}
