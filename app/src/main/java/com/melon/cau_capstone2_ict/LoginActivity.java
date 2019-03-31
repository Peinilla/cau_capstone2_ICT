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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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

        String id = login_id.getText().toString();
        String pass = login_pass.getText().toString();

        id = "test1234";
        pass = "123456";

        Response.Listener<String> responseListener = new Response.Listener<String> () {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String memberID = jsonResponse.getString("id");
                    String memberPW = jsonResponse.getString("password");

                    Log.d("Tag", "id : " + memberID + "/ pass : " + memberPW);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("Member_Name","test");
                    intent.putExtra("Member_ID","000000");
                    startActivity(intent);
                    finish();

//                    if (false) {
//
//                    }
//                    else {
//                        Log.d("Tag", "clicked2");
//                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                        dialog = builder.setMessage("fail").setNegativeButton("ok", null).create();
//                        dialog.show();
//                    }
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
                        final static private String URL = "https://capston2webapp.azurewebsites.net/api/Login";
                        private Map<String, String> parameters;

                        public LoginRequest(String id, String pw, Response.Listener<String> listener) {
                            super(Method.POST, URL, listener, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Tag", "error " + error);
                }
            }); // 해당 정보를 POST 방식으로 URL에 전송
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



