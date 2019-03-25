package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText login_id;
    private EditText login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = (EditText) findViewById(R.id.signup_id);
        login_pass = (EditText) findViewById(R.id.login_password);
    }

    public void onClickLogin(View v){
        //  ID/password check , Login

        // 임시
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Member_Name","TEST");
        intent.putExtra("Member_ID","000000");
        startActivity(intent);
    }

    public void onClickSignUp(View v){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void onClickSNS(View v){
        // SNS 연동
    }
}
