package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyChatAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;


public class TabFragment_chat extends Fragment implements MainActivity.OnBackPressedListener {
    MyChatAdapter adapter;

    FloatingActionsMenu fam;
    FloatingActionsMenu add;
    ListView chatList;
    TextView chatTitle;
    EditText myMessage;
    ImageButton sendBtn;

    Handler mHandler = new Handler();

    ////
    String to;
    ////


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_chat, container, false);

        to = getArguments().getString("ToUser");
        chatTitle = rootView.findViewById(R.id.chat_title);
        myMessage = rootView.findViewById(R.id.textView_chat);
        sendBtn = rootView.findViewById(R.id.image_upload);
        add = (FloatingActionsMenu)rootView.findViewById(R.id.chat_add);
        fam = (FloatingActionsMenu)rootView.findViewById(R.id.chat_floatingButton);
        chatList = (ListView) rootView.findViewById(R.id.chat_list);

        chatTitle.setText(to);

        adapter = new MyChatAdapter();

        chatList.setAdapter(adapter);
        add.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fam.expand();
            }

            @Override
            public void onMenuCollapsed() {
                fam.collapse();
            }
        });

        sendBtn.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Tag", "send message :  " + to + " : ");
                ChatHubManager.getInstance().send(to,myMessage.getText().toString());
                myMessage.setText("");
            }
        });

        ChatHubManager.getInstance().getHubProxy().on("sendMessage", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(final String s, final String s2) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getMessage(s,s2);
                    }
                });
            }
        },String.class,String.class);

        return  rootView;
    }


    void getMessage(String s, String s2){
        if(s2.equals(to)) {
            adapter.addItem(2, s, s2);
        }else{
            adapter.addItem(0,s,MyUserData.getInstance().getNickname());
        }
        adapter.notifyDataSetChanged();
        chatList.setAdapter(adapter);
        chatList.setSelection(adapter.getCount() - 1);
    }
    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.chat_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
        goBack();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }

    @Override
    public void onStop() {
        Log.d("Tag", "on stop");
        super.onStop();
        super.onDetach();
    }
}

