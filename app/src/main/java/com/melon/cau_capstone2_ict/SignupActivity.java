package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText signup_id, signup_pass, signup_passConf,signup_email;
    private Button signup_validate;
    private AlertDialog dialog;
    boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_id = (EditText)findViewById(R.id.signup_id);
        signup_validate = (Button) findViewById(R.id.button_validateID);
        signup_pass = (EditText)findViewById(R.id.signup_pass);
        signup_passConf = (EditText)findViewById(R.id.signup_passConfirm);
        signup_email = (EditText)findViewById(R.id.signup_email);
    }

    public void onClickValidate(View v) {
        // ID 중복 체크
        validate = true;
    }

    public void onClickNext(View v) {
        String id = signup_id.getText().toString();
        String pass = signup_pass.getText().toString();
        String passConf = signup_passConf.getText().toString();
        String email = signup_email.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

        if (!validate) {
            dialog = builder.setMessage("아이디 중복 체크를 해주세요.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }
        else if(!pass.equals(passConf)){
            dialog = builder.setMessage("비밀번호 확인이 일치하지 않습니다.").setPositiveButton("OK", null).create();
            dialog.show();

            signup_pass.setText("");
            signup_passConf.setText("");
            return;
        }
        else if (pass.length() < 4) {
            dialog = builder.setMessage("비밀번호는 4자 이상이어야 합니다.").setPositiveButton("OK", null).create();
            dialog.show();
            return;
        }
        else if (id.equals("") || pass.equals("") || passConf.equals("")) {
            dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else{
            Intent intent = new Intent(SignupActivity.this, Signup2Activity.class);
            intent.putExtra("Member_ID",id);
            intent.putExtra("Member_pass",pass);
            intent.putExtra("Member_email",email);

            startActivity(intent);
        }
    }

    public void onClickBack(View v){
        finish();
    }
}
