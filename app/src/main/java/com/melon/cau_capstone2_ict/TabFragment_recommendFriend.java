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
import android.widget.ArrayAdapter;

import android.widget.ListView;


import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
import com.melon.cau_capstone2_ict.Manager.OnBackManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_recommendFriend extends Fragment implements MainActivity.OnBackPressedListener{

    ListView recolistview;

    static ArrayList<String> array_nickname = new ArrayList<>();

    ArrayAdapter reco_adapter;

    int containerID;
    Handler mHandler = new Handler();
    ArrayList<String> friendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_recommend, container, false);
        containerID = getArguments().getInt("ContainerID");
        recolistview = rootView.findViewById(R.id.reco_list);

        reco_adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, array_nickname);
        recolistview.setAdapter(reco_adapter);

        friendList = new ArrayList<>();
        friendList.addAll(MyUserData.getInstance().getFriendList());

        ChatHubManager.getInstance().getHubProxygroup().on("getRightNowTagUserInfo", new SubscriptionHandler1<String>() {
            @Override
            public void run(final String s) {

                array_nickname.clear();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("nickname");
                        if(!name.equals(MyUserData.getInstance().getNickname())) {
                            if(!friendList.contains(name)) {
                                array_nickname.add(name);
                            }
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getRecommend();
                            reco_adapter.notifyDataSetChanged();
                            recolistview.setAdapter(reco_adapter);
                        }
                    });

                }catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, String.class);

        recolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), profileViewActivity.class);
                intent.putExtra("id",(String)parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        ChatHubManager.getInstance().getHubProxygroup().invoke("GetRightnowTagUserInfo",MyUserData.getInstance().getId());

        return rootView;
    }
    void getRecommend(){
        if(array_nickname.size() > 6){

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
}