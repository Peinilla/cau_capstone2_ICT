package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText login_id;
    private EditText login_pass;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = (EditText) findViewById(R.id.signup_id);
        login_pass = (EditText) findViewById(R.id.login_password);
    }

    public void onClickLogin(View v){
        //  ID/password check , Login

        String id = "test1234";
        String pass = "123456";

        Response.Listener<String> responseListener = new Response.Listener<String> () {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response" + response);

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage(response).setNegativeButton("ok", null).create();
                    dialog.show();

                    if (false) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("Member_Name","test");
                        intent.putExtra("Member_ID","000000");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Log.d("Tag", "clicked2");
                    }
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(id, pass, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

    }

    public void onClickSignUp(View v){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void onClickSNS(View v){
        // SNS 연동
    }
    class LoginRequest extends StringRequest {
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/Register";
        private Map<String, String> parameters;

        public LoginRequest(String id, String pw, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null); // 해당 정보를 POST 방식으로 URL에 전송
            Log.d("Tag", "LoginRequest");
            parameters = new HashMap<>();
            parameters.put("id", id);
            parameters.put("password", pw);
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
}



