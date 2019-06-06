package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profileViewActivity extends AppCompatActivity {

    String receiverNick;
    boolean isNFC;
    TextView idView;

    TextView birthView;
    TextView residendceView;
    TextView hobbyView;
    TextView majorView;

    Button btn_addFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Intent intent;
        intent = getIntent();

        receiverNick = intent.getStringExtra("id");
        isNFC = intent.getBooleanExtra("isNFC",false);

        idView = findViewById(R.id.profileview_ID);
        birthView = findViewById(R.id.profileview_birth);
        residendceView = findViewById(R.id.profileview_residence);
        hobbyView = findViewById(R.id.profileview_hobby);
        majorView = findViewById(R.id.profileview_dept);

        btn_addFriend = findViewById(R.id.profileview_addFriend);
        idView.setText(receiverNick);

        getProfile();

        if(MyUserData.getInstance().getFriendList().contains(receiverNick)){
            if(isNFC){
                btn_addFriend.setText("NFC 친구 신청");
            }else{
                btn_addFriend.setText("이미 친구입니다.");
                btn_addFriend.setClickable(false);
            }
        }else{
            if(isNFC){
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(profileViewActivity.this);
                dialog = builder.setMessage("NFC 친구가 되기 전에 먼저 온라인친구가 되어야합니다.").setNegativeButton("확인", null).create();
                dialog.show();
            }
        }

    }

    public void addFriendClick(View v){
        addFreind();
    }

    void addFreind(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean result = jsonResponse.getBoolean("ret");
                    if(result){
                        Toast.makeText(getApplicationContext(),"친구 신청을 보냈습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"친구 신청 실패 " + jsonResponse.getBoolean("resson"),Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if(isNFC){
            addNFCRequest request = new addNFCRequest(responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        }else{
            addFriendRequest request = new addFriendRequest(responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        }

    }
    class addFriendRequest extends StringRequest {
        private Map<String, String> parameters;
        public addFriendRequest(Response.Listener<String> listener) {
            super(Method.POST, "https://capston2webapp.azurewebsites.net/api/"+MyUserData.getInstance().getId() + "/friend/add", listener, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Tag", "addFriendRequest " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("senderId", MyUserData.getInstance().getId());
            parameters.put("receiverNick", receiverNick);
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
    class addNFCRequest extends StringRequest {
        private Map<String, String> parameters;
        public addNFCRequest(Response.Listener<String> listener) {
            super(Method.POST, "https://capston2webapp.azurewebsites.net/api/"+MyUserData.getInstance().getId() + "/friend/update", listener, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "error " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("senderId", MyUserData.getInstance().getId());
            parameters.put("receiverNick", receiverNick);
            parameters.put("type","2");
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
    public void getProfile(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getString("dateofbirth").equals("null")){
                        birthView.setText(jsonObject.getString("dateofbirth"));
                    }
                    if(!jsonObject.getString("residence").equals("null")){
                        residendceView.setText(jsonObject.getString("residence"));
                    }
                    if(!jsonObject.getString("major").equals("null")){
                        majorView.setText(jsonObject.getString("major"));
                    }
                    if(!jsonObject.getString("hobby").equals("null")){
                        hobbyView.setText(jsonObject.getString("hobby"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/UserInfo/"+ receiverNick;
        StringRequest getBoardRequest = new StringRequest(Request.Method.GET,URL,responseListener,null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getBoardRequest);
    }
}
