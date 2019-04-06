package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText signup_id, signup_pass, signup_passConf,signup_email;
    private Button signup_validate;
    private AlertDialog dialog;
    boolean isValidate = false;

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
        String memberID = signup_id.getText().toString();

        if (isValidate) {
            return;
        }

        if (memberID.length() < 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            dialog = builder.setMessage("아이디는 5자 이상이어야 합니다.").setNegativeButton("Retry", null).create();
            dialog.show();
        }
        else{
            validationCheck(memberID);
        }
    }

    public void onClickNext(View v) {
        String id = signup_id.getText().toString();
        String pass = signup_pass.getText().toString();
        String passConf = signup_passConf.getText().toString();
        String email = signup_email.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

        if (!isValidate) {
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

    public void validationCheck(String memberID){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("Tag", "response : " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("return");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "사용할 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        isValidate = true;
                        signup_validate.setBackgroundColor(Color.GRAY);
                    } else {
                        dialog = builder.setMessage("이미 사용 중인 아이디입니다.").setNegativeButton("Retry", null).create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ValidateRequest validateRequest = new ValidateRequest(memberID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
        queue.add(validateRequest);
    }

    class ValidateRequest extends StringRequest {
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/Verify";
        private Map<String, String> parameters;

        public ValidateRequest(String id, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "error " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("valueName", "id");
            parameters.put("value", id);
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
}
