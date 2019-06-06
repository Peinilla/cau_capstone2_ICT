package com.melon.cau_capstone2_ict;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyFriendAdapter;
import com.melon.cau_capstone2_ict.Manager.MyFriendinfo;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_profile extends Fragment {

    TextView idView;
    TextView friendRequest_TextView;
    ListView friendListView;
    ListView friendRequestView;

    FloatingActionButton btn_nfc_Add;
    FloatingActionButton btn_recoFriend;
    FloatingActionButton btn_set;


    SwipeRefreshLayout srl1;
    SwipeRefreshLayout srl2;

    static ArrayList<String> friendRequestArray = new ArrayList<>();

    MyFriendAdapter myFriendAdapter;
    ArrayAdapter friendRequest_adapter;

    FrameLayout chatContainer;
    Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        idView = rootView.findViewById(R.id.profile_ID);
        friendRequest_TextView = rootView.findViewById(R.id.profile_textview4);
        friendListView = rootView.findViewById(R.id.friend_list);
        friendRequestView = rootView.findViewById(R.id.friendrequest_list);
        chatContainer = rootView.findViewById(R.id.profile_container);
        btn_nfc_Add = rootView.findViewById(R.id.button_nfc);
        btn_recoFriend = rootView.findViewById(R.id.button_reco);
        btn_set = rootView.findViewById(R.id.button_set);

        idView.setText(MyUserData.getInstance().getNickname());

        srl1 = rootView.findViewById(R.id.profile_swipe);
        srl2 = rootView.findViewById(R.id.profile_swipe2);

        myFriendAdapter = new MyFriendAdapter();
        friendListView.setAdapter(myFriendAdapter);

        friendRequest_adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, friendRequestArray);
        friendListView.setAdapter(friendRequest_adapter);

        ChatHubManager.getInstance().getHubProxy().removeSubscription("getuserlist");
        ChatHubManager.getInstance().getHubProxy().on("getUserList", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try { // we added the list of connected users
//                    Log.d("Tag", "현재접속유저 : " + s);
//
//                    JSONArray jsonArray = new JSONArray(s);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            myFriendAdapter.notifyDataSetChanged();
//                            friendListView.setAdapter(myFriendAdapter);
                        }
                    });
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, String.class);

        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment childFragment = new TabFragment_chat();
                Bundle bundle = new Bundle(1);
                MyFriendinfo m = (MyFriendinfo)parent.getItemAtPosition(position);
                bundle.putString("ToUser",m.getId());
                bundle.putString("ToUserNick",m.getNickname());

                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.profile_container, childFragment).commit();
            }
        });

        friendRequestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String str = (String)parent.getItemAtPosition(position);
                final AlertDialog alert = new AlertDialog.Builder(getActivity())
                        .setTitle(str + "님의 친구요청")
                        .setCancelable(true)
                        .setPositiveButton("수락",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        friendUpdate(str,"1");
                                    }
                                })
                        .setNegativeButton("거절", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendUpdate(str,"0");
                            }
                        })
                        .create();
                alert.show();
            }
        });


        /////
        btn_nfc_Add.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NfcActivity.class);
                startActivity(intent);
            }
        });
        btn_set.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActvity.class);
                startActivity(intent);
            }
        });
        btn_recoFriend.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment childFragment = new TabFragment_recommendFriend();
                Bundle bundle = new Bundle(1);
                bundle.putInt("ContainerID",R.id.profile_container);
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.profile_container, childFragment).commit();
            }
        });

        getFriendList();
        getFriendRequestList();

        srl1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl1.setRefreshing(false);
                getFriendList();
            }
        });
        srl2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl2.setRefreshing(false);
                getFriendRequestList();
            }
        });


        return rootView;
    }

    public void getFriendList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    myFriendAdapter.clear();
                    MyUserData.getInstance().clearFriendList();
                    JSONArray array = new JSONArray(response);
                    for(int inx = 0; inx < array.length(); inx++) {
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        myFriendAdapter.addItem(jsonResponse.getString("nick"),jsonResponse.getString("id"),jsonResponse.getInt("type"));
                        MyUserData.getInstance().addFriend(jsonResponse.getString("nick"));
                    }
                    myFriendAdapter.notifyDataSetChanged();
                    friendListView.setAdapter(myFriendAdapter);
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/"+MyUserData.getInstance().getId() + "/friends";
        StringRequest getBoardRequest = new StringRequest(Request.Method.GET,URL,responseListener,null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getBoardRequest);
    }

    public void getFriendRequestList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    friendRequestArray.clear();
                    JSONArray array = new JSONArray(response);
                    for(int inx = 0; inx < array.length(); inx++) {
                        friendRequestArray.add(array.getString(inx));
                    }
                    friendRequest_adapter.notifyDataSetChanged();
                    friendRequestView.setAdapter(friendRequest_adapter);
                    if(friendRequest_adapter.getCount() == 0){
                        srl2.setVisibility(View.GONE);
                        friendRequest_TextView.setVisibility(View.GONE);
                    }else{
                        srl2.setVisibility(View.VISIBLE);
                        friendRequest_TextView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/"+MyUserData.getInstance().getId() + "/friend/requests";
        StringRequest getRequest = new StringRequest(Request.Method.GET,URL,responseListener,null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getRequest);
    }

    void friendUpdate(String receiverNick,String type){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Tag",response.toString());
                    boolean result = response.getBoolean("ret");
                    if(result){
                        getFriendList();
                        getFriendRequestList();
                    }else{
                        getFriendRequestList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("senderId", MyUserData.getInstance().getId());
            jsonObject.put("receiverNick", receiverNick);
            jsonObject.put("type",Integer.valueOf(type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        friendUpdateRequest request = new friendUpdateRequest(jsonObject,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    class friendUpdateRequest extends JsonObjectRequest {
        private Map<String, String> parameters;

        public friendUpdateRequest(JSONObject jsonObject,Response.Listener<JSONObject> listener) {
            super(Method.POST, "https://capston2webapp.azurewebsites.net/api/"+MyUserData.getInstance().getId() + "/friend/update", jsonObject,listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "error " + error);
                }
            });
            parameters = new HashMap<>();

        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriendList();
        getFriendRequestList();
    }
}