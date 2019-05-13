package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_nowim extends Fragment {


    ImageButton btn_upload;
    EditText tagText;
    ListView tagListView;
    Button btn_chatroom;
    FrameLayout chatContainer;

    static ArrayList<String> tagArray = new ArrayList<>();
    static ArrayList<String> chatroomArray = new ArrayList<>();

    ArrayAdapter adapter;

    Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_now, container, false);

        btn_upload = rootView.findViewById(R.id.now_tagupload);
        btn_chatroom = rootView.findViewById(R.id.now_Chatroom);
        tagText = rootView.findViewById(R.id.now_tag);
        tagListView = rootView.findViewById(R.id.now_taglist);
        chatContainer = rootView.findViewById(R.id.now_container);
        chatContainer.bringToFront();

        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,tagArray);
        tagListView.setAdapter(adapter);

        ChatHubManager.getInstance().getHubProxygroup().on("updateAvailableTags", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d("Tag", "response " + s);

                    tagArray.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        tagArray.add(jsonArray.getString(i));
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            tagListView.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, String.class);
        ChatHubManager.getInstance().getHubProxygroup().on("updateRightNowChatList", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    chatroomArray.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        chatroomArray.add(jsonArray.getString(i));
                    }
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, String.class);

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = (String)parent.getItemAtPosition(position);
                if(!chatroomArray.contains(tag)) {
                    Fragment childFragment = new TabFragment_chatGroup();
                    Bundle bundle = new Bundle(1);
                    bundle.putString("GroupID",tag);
                    bundle.putInt("ContainerID",R.id.now_container);
                    childFragment.setArguments(bundle);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.now_container, childFragment).commit();
                }else{
                    Toast.makeText(getActivity(), "인원이 충분하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_upload.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChatHubManager.getInstance().setTag(tagText.getText().toString());
            }
        });

        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


}