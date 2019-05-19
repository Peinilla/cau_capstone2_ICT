package com.melon.cau_capstone2_ict;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup2Activity extends AppCompatActivity {

    private String id;
    private String pass;
    private String email;
    private String name,home,birth,hobby,dept;
    private EditText signup_name, signup_birth, signup_hobby, signup_email;
    private Button signup_validate;
    private AlertDialog dialog;
    boolean validateName = false;

    Spinner spinner_home;
    Spinner spinner_dept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        signup_name = (EditText)findViewById(R.id.signup_name);
        signup_validate = (Button) findViewById(R.id.button_validateName);
        signup_hobby = (EditText)findViewById(R.id.signup_hobby);
        signup_birth = (EditText)findViewById(R.id.signup_birth);
        signup_email = (EditText)findViewById(R.id.signup_email);

        home = "";
        dept = "";

        Intent intent;
        intent = getIntent();

        id = intent.getStringExtra("Member_ID");
        pass = intent.getStringExtra("Member_pass");

        spinner_home = (Spinner)findViewById(R.id.signup_home);
        spinner_home.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    home = (String)adapterView.getItemAtPosition(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        String[] str_home = getResources().getStringArray(R.array.select_home_item);
        ArrayAdapter<String> home_adapter= new ArrayAdapter<String>(this,R.layout.spinner_home,str_home);
        home_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_home.setAdapter(home_adapter);

        spinner_dept = (Spinner)findViewById(R.id.signup_dept);
        spinner_dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    dept = (String)adapterView.getItemAtPosition(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        String[] str_dept = getResources().getStringArray(R.array.select_dept_item);
        ArrayAdapter<String> dept_adapter= new ArrayAdapter<String>(this,R.layout.spinner_home,str_dept);
        dept_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_dept.setAdapter(dept_adapter);
    }

    public void onClickValidate(View v) {
        String memberID = signup_name.getText().toString();

        if (validateName) {
            return;
        }

        if (memberID.length() < 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Signup2Activity.this);
            dialog = builder.setMessage("닉네임은 3자 이상이어야 합니다.").setNegativeButton("Retry", null).create();
            dialog.show();
        }
        else{
            validationCheck(memberID);
        }
    }

    public void onClickNext(View v) {
        email = signup_email.getText().toString();
        name = signup_name.getText().toString();
        birth = signup_birth.getText().toString();
        hobby = signup_hobby.getText().toString();

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
        else if(home.equals("")){
            dialog = builder.setMessage("거주지를 선택해주세요.").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else if(dept.equals("")){
            dialog = builder.setMessage("학과를 선택해주세요.").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else if(birth.length() != 8){
            dialog = builder.setMessage("생년월일을 입력해주세요.").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else if(hobby.equals("")){
            dialog = builder.setMessage("취미를 입력해주세요..").setNegativeButton("OK", null).create();
            dialog.show();
            return;
        }
        else{

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("Tag", "register response : " + response);

                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup2Activity.this);
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            RegisterRequest registerRequest = new RegisterRequest(id, pass, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Signup2Activity.this);
            queue.add(registerRequest);

            //
            // 회원가입 절차
            //
        }
    }

    public void onClickBack(View v){
        finish();
    }

    public void validationCheck(final String nickname){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response nickname check : " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("return");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup2Activity.this);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "사용할 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        validateName = true;
                        signup_validate.setBackgroundColor(Color.GRAY);
                        signup_name.setFocusable(false);
                        signup_name.setClickable(false);
                    } else {
                        dialog = builder.setMessage("이미 사용 중인 아이디입니다.").setNegativeButton("Retry", null).create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ValidateNameRequest validateRequest = new ValidateNameRequest(nickname, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Signup2Activity.this);
        queue.add(validateRequest);
    }

    class RegisterRequest extends StringRequest {
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/AddProfile";
        private Map<String, String> parameters;

        public RegisterRequest(String id, String pw, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "Register error : " + error.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup2Activity.this);
                    dialog = builder.setMessage("Failed").setNegativeButton("Retry", null).create();
                    dialog.show();
                }
            });
            parameters = new HashMap<>();

            parameters.put("email", email);
            parameters.put("nickname", name);
            parameters.put("dateofbirth", birth);
            parameters.put("residence", home);
            parameters.put("major", dept);
            parameters.put("hobby", hobby);
            parameters.put("id", id);
            parameters.put("password", pw);

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


    class ValidateNameRequest extends StringRequest {
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/Verify";
        private Map<String, String> parameters;

        public ValidateNameRequest(String name, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            Log.d("Tag", "ValidateRequest");
            parameters = new HashMap<>();

            parameters.put("valueName", "nickname");
            parameters.put("value", name);
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
}
