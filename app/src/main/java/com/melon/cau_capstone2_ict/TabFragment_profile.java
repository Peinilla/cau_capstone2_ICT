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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_profile extends Fragment {

    TextView idView;
    ListView chatListView;

    ArrayList<String> chatArray;
    ArrayAdapter adapter;

    FrameLayout chatContainer;
    Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        idView = rootView.findViewById(R.id.profile_ID);
        chatListView = rootView.findViewById(R.id.chat_roomlist);
        chatContainer = rootView.findViewById(R.id.chat_container);

        idView.setText(MyUserData.getInstance().getNickname());

        chatArray = new ArrayList<>();
        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,chatArray);

        ChatHubManager.getInstance().connect();
        ChatHubManager.getInstance().getHubProxy().on("getUserList", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try { // we added the list of connected users
                    JSONArray jsonArray = new JSONArray(s);
                    chatArray.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        chatArray.add(username);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            chatListView.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, String.class);
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment childFragment = new TabFragment_chat();

                Bundle bundle = new Bundle(1);
                bundle.putString("ToUser",(String)parent.getItemAtPosition(position));

                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.chat_container, childFragment).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
