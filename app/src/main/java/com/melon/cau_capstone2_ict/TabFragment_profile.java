package com.melon.cau_capstone2_ict;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_profile extends Fragment {

    TextView idView;
    ListView friendListView;
    ListView friendRequestView;

    FloatingActionButton btn_nfc_Add;

    static ArrayList<String> friendArray = new ArrayList<>();
    static ArrayList<String> friendRequestArray = new ArrayList<>();

    ArrayAdapter friend_adapter;
    ArrayAdapter friendRequest_adapter;

    FrameLayout chatContainer;
    Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        idView = rootView.findViewById(R.id.profile_ID);
        friendListView = rootView.findViewById(R.id.friend_list);
        friendRequestView = rootView.findViewById(R.id.friendrequest_list);
        chatContainer = rootView.findViewById(R.id.profile_container);
        btn_nfc_Add = rootView.findViewById(R.id.button_nfc);
        idView.setText(MyUserData.getInstance().getNickname());

        friend_adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, friendArray);
        friendListView.setAdapter(friend_adapter);

        friendRequest_adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, friendRequestArray);
        friendListView.setAdapter(friendRequest_adapter);


        ChatHubManager.getInstance().connect();
        ChatHubManager.getInstance().getHubProxy().removeSubscription("getuserlist");
        ChatHubManager.getInstance().getHubProxy().on("getUserList", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try { // we added the list of connected users
                    JSONArray jsonArray = new JSONArray(s);
                    friendArray.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        //현재 접속유저 확인
                        //friendArray.add(username);
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            friend_adapter.notifyDataSetChanged();
                            friendListView.setAdapter(friend_adapter);
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
                bundle.putString("ToUser",(String)parent.getItemAtPosition(position));

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

        getFriendList();
        getFriendRequestList();

        return rootView;
    }
    public void getFriendList(){
        friendArray.clear();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "친구목록  : " + response);

                    String res = response.substring(2,response.length()-2);
                    res = res.replace("\"","");
                    res = res.replace("\\","");
                    String[] tmp = res.split(",");
                    for(int inx = 0; inx < tmp.length; inx++) {
                        if(!tmp[inx].equals("")) {
                            friendArray.add(tmp[inx]);
                            friend_adapter.notifyDataSetChanged();
                            friendListView.setAdapter(friend_adapter);
                        }

                    }
                } catch (Exception e) {
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
        friendRequestArray.clear();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "친구대기 목록  : " + response);

                    String res = response.substring(2,response.length()-2);
                    res = res.replace("\"","");
                    res = res.replace("\\","");
                    String[] tmp = res.split(",");
                    for(int inx = 0; inx < tmp.length; inx++) {
                        if(!tmp[inx].equals("")) {
                            friendRequestArray.add(tmp[inx]);
                            friendRequest_adapter.notifyDataSetChanged();
                            friendRequestView.setAdapter(friendRequest_adapter);
                        }

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
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "done  : " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean result = jsonResponse.getBoolean("ret");
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
        friendUpdateRequest request = new friendUpdateRequest(receiverNick,type,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    class friendUpdateRequest extends StringRequest {
        private Map<String, String> parameters;

        public friendUpdateRequest(String receiverNick,String type,Response.Listener<String> listener) {
            super(Method.POST, "https://capston2webapp.azurewebsites.net/api/"+MyUserData.getInstance().getId() + "/friend/update", listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "error " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("senderId", MyUserData.getInstance().getId());
            parameters.put("receiverNick", receiverNick);
            parameters.put("type",type);

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
}