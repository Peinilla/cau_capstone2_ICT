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
import com.melon.cau_capstone2_ict.Manager.OnBackManager;

import org.json.JSONArray;
import org.json.JSONObject;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;


public class TabFragment_chatBab extends Fragment implements MainActivity.OnBackPressedListener {
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

    String str1;
    String str2;

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
                String msg = myMessage.getText().toString();
                if(!msg.equals("")){
                    ChatHubManager.getInstance().getHubProxy_bab().invoke("SendGroupMsg", MyUserData.getInstance().getId(),msg);
                }
                myMessage.setText("");
            }
        });

        ChatHubManager.getInstance().getHubProxygroup().on("onBopPartyMsgReceived", new SubscriptionHandler1<String>() {
            @Override
            public void run(final String s) {
                try {
                    JSONObject jsonResponse = new JSONObject(s);
                    str1 = jsonResponse.getString("text");
                    str2 = jsonResponse.getString("userNick");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getMessage(str1,str2);
                        }
                    });
                }catch (Exception e){
                    Log.e("Tag", "onGroupChat Error" + e.getMessage());
                    e.printStackTrace();
                }
            }
        },String.class);

        ChatHubManager.getInstance().getHubProxy_bab().invoke("GetMsgByIndex", MyUserData.getInstance().getId(), group, 0);
        ChatHubManager.getInstance().getHubProxy_bab().on("updateGroupChatByIndex", new SubscriptionHandler2<String, String>() {
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
                            adapter.updateDate();
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
        if(!MyUserData.getInstance().getNickname().equals(s2)) {
            adapter.addItem(2, s, s2);
        }else{
            adapter.addItem(0,s,MyUserData.getInstance().getNickname());
        }
        adapter.updateDate();
        adapter.notifyDataSetChanged();
        chatList.setAdapter(adapter);
        chatList.setSelection(adapter.getCount() - 1);
    }
    void getMessage(String s, String s2,String date){
        if(s2.equals(MyUserData.getInstance().getNickname())) {
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
        OnBackManager.getInstance().removeOnBackList();
        Object o = OnBackManager.getInstance().getOnBackList();
        activity.setOnBackPressedListener((MainActivity.OnBackPressedListener) o);
        goBack();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Object o = this;
        OnBackManager.getInstance().setOnBackList(o);

        ((MainActivity)context).setOnBackPressedListener(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        super.onDetach();
    }
}

