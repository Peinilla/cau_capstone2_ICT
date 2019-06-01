package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyChatAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONObject;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;


public class TabFragment_chatGroup extends Fragment implements MainActivity.OnBackPressedListener {
    MyChatAdapter adapter;

    FloatingActionsMenu fam;
    FloatingActionsMenu add;
    ListView chatList;
    TextView chatTitle;
    EditText myMessage;
    ImageButton sendBtn;

    Handler mHandler = new Handler();
    String group;
    int containerID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_chat, container, false);

        group = getArguments().getString("GroupID");
        containerID = getArguments().getInt("ContainerID");
        chatTitle = rootView.findViewById(R.id.chat_title);
        myMessage = rootView.findViewById(R.id.textView_chat);
        sendBtn = rootView.findViewById(R.id.image_upload);
        add = (FloatingActionsMenu)rootView.findViewById(R.id.chat_add);
        fam = (FloatingActionsMenu)rootView.findViewById(R.id.chat_floatingButton);
        chatList = (ListView) rootView.findViewById(R.id.chat_list);


        chatTitle.setText(group + " (그룹)");

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
                ChatHubManager.getInstance().group_send(myMessage.getText().toString());
                myMessage.setText("");
            }
        });

        ChatHubManager.getInstance().getHubProxygroup().on("onGroupChat", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(final String s, final String s2) {
                try {
                    JSONObject jsonResponse = new JSONObject(s2);
                    getMessage(jsonResponse.getString("text"), jsonResponse.getString("userNick"));
                }catch (Exception e){
                    Log.e("Tag", "onGroupChat Error" + e.getMessage());
                    e.printStackTrace();
                }
            }
        },String.class,String.class);

        ChatHubManager.getInstance().getHubProxygroup().invoke("GetRightnowMessageByIndex", MyUserData.getInstance().getId(), group, 0);
        ChatHubManager.getInstance().getHubProxygroup().on("updateGroupChatByIndex", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(String s1, String s2) {
                try {
                    Log.d("Tag", "getChatLog : " + s2);
                    JSONArray array = new JSONArray(s2);

                    for(int inx = 0; inx < array.length(); inx++) {
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        getMessage(jsonResponse.getString("text"),jsonResponse.getString("userNick"),jsonResponse.getString("time"));
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            chatList.setAdapter(adapter);
                            chatList.setSelection(adapter.getCount() - 1);
                        }
                    });

                }catch (Exception e){
                    Log.e("Tag", "receive Message Error" + e.getMessage());
                    e.printStackTrace();
                }
            }
        },String.class, String.class);

        return  rootView;

    }

    void getMessage(String s, String s2){
        if(!MyUserData.getInstance().getId().equals(s2)) {
            adapter.addItem(2, s, s2);
        }else{
            adapter.addItem(0,s,MyUserData.getInstance().getNickname());
        }
        adapter.notifyDataSetChanged();
        chatList.setAdapter(adapter);
        chatList.setSelection(adapter.getCount() - 1);
    }
    void getMessage(String s, String s2,String date){
        if(s2.equals(MyUserData.getInstance().getId())) {
            adapter.addItem(0,s, MyUserData.getInstance().getNickname(),date);
        }else{
            adapter.addItem(2, s, s2,date);
        }
    }
    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(containerID);
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
        super.onStop();
        super.onDetach();
    }
}

