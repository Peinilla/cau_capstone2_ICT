package com.melon.cau_capstone2_ict;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.melon.cau_capstone2_ict.Manager.HttpConectManager;

import java.util.ArrayList;

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
        // 회원가입으로 테스트중
        String url = "https://capston2webapp.azurewebsites.net/api/Register";
        ContentValues cv = new ContentValues();
        cv.put("id","qwer123456");
        cv.put("password","1234");



        loginTask task = new loginTask(url,cv);
        task.execute();

        // 임시
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Member_Name","TEST");
        intent.putExtra("Member_ID","000000");
        //startActivity(intent);
    }

    public void onClickSignUp(View v){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void onClickSNS(View v){
        // SNS 연동
    }

    public class loginTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public loginTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            HttpConectManager requestHttpURLConnection = new HttpConectManager();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

            dialog = builder.setMessage(s).setPositiveButton("OK", null).create();
            dialog.show();

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        }
    }
}
