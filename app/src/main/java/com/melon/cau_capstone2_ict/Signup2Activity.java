package com.melon.cau_capstone2_ict;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup2Activity extends AppCompatActivity {

    private String ID;
    private String pass;
    private String email;
    private EditText signup_name, signup_home, signup_birth,signup_dept, signup_hobby;
    private Button signup_validate;
    private AlertDialog dialog;
    boolean validateName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        signup_name = (EditText)findViewById(R.id.signup_name);
        signup_validate = (Button) findViewById(R.id.button_validateName);
        signup_home = (EditText)findViewById(R.id.signup_home);
        signup_dept = (EditText)findViewById(R.id.signup_dept);
        signup_hobby = (EditText)findViewById(R.id.signup_hobby);
        signup_birth = (EditText)findViewById(R.id.signup_birth);

        Intent intent;
        intent = getIntent();

        ID = intent.getStringExtra("Member_ID");
        pass = intent.getStringExtra("Member_Name");
        email = intent.getStringExtra("Member_email");
    }

    public void onClickValidate(View v) {
        // ID 중복 체크
        validateName = true;
    }

    public void onClickNext(View v) {
        String name = signup_name.getText().toString();
        String home = signup_home.getText().toString();
        String dept = signup_dept.getText().toString();
        String birth = signup_birth.getText().toString();
        String hobby = signup_hobby.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(Signup2Activity.this);

        if (!validateName) {
            dialog = builder.setMessage("닉네임 중복 체크를 해주세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }
        else if (name.equals("")) {
            dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else if(birth.length() != 10){
            dialog = builder.setMessage("생년월일을 입력해주세요.").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else{

            //
            // 회원가입 절차
            //

            boolean success = true; // 회원가입 결과

            if (success) {
                builder.setMessage("회원등록이 완료되었습니다!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create();
                builder.show();
            } else {
                dialog = builder.setMessage("Failed").setNegativeButton("Retry", null).create();
                dialog.show();
            }

        }
    }

    public void onClickBack(View v){
        finish();
    }
}
