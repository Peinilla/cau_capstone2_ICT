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


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
import com.melon.cau_capstone2_ict.Manager.OnBackManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        getRecommend();
        ChatHubManager.getInstance().getHubProxygroup().invoke("GetRightnowTagUserInfo",MyUserData.getInstance().getId());

        return rootView;
    }

    void getRecommend(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "getRecommend check : " + response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int inx = 0; inx < jsonArray.length(); inx ++){
                        JSONObject jsonObject = jsonArray.getJSONObject(inx);
                        String str = jsonObject.getString("userNick");
                        if(!friendList.contains(str) && !MyUserData.getInstance().getNickname().equals(str)) {
                            array_nickname.add(str);
                        }
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            reco_adapter.notifyDataSetChanged();
                            recolistview.setAdapter(reco_adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/userinfo/recommend";
        getRecommendRequest req = new getRecommendRequest(URL,responseListener);
        req.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(req);
    }
    class getRecommendRequest extends StringRequest {
        private Map<String, String> parameters;

        public getRecommendRequest(String URL, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "getRecommendRequest error : " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("residence", MyUserData.getInstance().getResidence());
            parameters.put("hobby", MyUserData.getInstance().getHobby());
            parameters.put("major", MyUserData.getInstance().getMajor());
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
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