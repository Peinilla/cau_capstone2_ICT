package com.melon.cau_capstone2_ict;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActvity extends AppCompatActivity {

    TextView text_name;
    TextView text_email;
    TextView text_birth;
    TextView text_residence;
    TextView text_major;
    TextView text_hobby;

    RadioButton rbtn_birth_1;
    RadioButton rbtn_birth_2;
    RadioButton rbtn_residence_1;
    RadioButton rbtn_residence_2;
    RadioButton rbtn_major_1;
    RadioButton rbtn_major_2;
    RadioButton rbtn_hobby_1;
    RadioButton rbtn_hobby_2;

    Button btn_modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_actvity);

        text_name = findViewById(R.id.profilemodify_name);
        text_email = findViewById(R.id.profilemodify_email_text);
        text_birth = findViewById(R.id.profilemodify_birth_text);
        text_residence = findViewById(R.id.profilemodify_residence_text);
        text_major = findViewById(R.id.profilemodify_major_text);
        text_hobby = findViewById(R.id.profilemodify_hobby_text);

        rbtn_birth_1 = findViewById(R.id.profilemodify_birth_but1);
        rbtn_birth_2  = findViewById(R.id.profilemodify_birth_but2);
        rbtn_residence_1 = findViewById(R.id.profilemodify_residence_but1);
        rbtn_residence_2 = findViewById(R.id.profilemodify_residence_but2);
        rbtn_major_1 = findViewById(R.id.profilemodify_major_but1);
        rbtn_major_2 = findViewById(R.id.profilemodify_major_but2);
        rbtn_hobby_1 = findViewById(R.id.profilemodify_hobby_but1);
        rbtn_hobby_2 = findViewById(R.id.profilemodify_hobby_but2);

        btn_modify = findViewById(R.id.profilemodify_modify);

        text_name.setText(MyUserData.getInstance().getNickname());
        text_email.setText(MyUserData.getInstance().getEmail());
        text_birth.setText(MyUserData.getInstance().getBirth());
        text_residence.setText(MyUserData.getInstance().getResidence());
        text_major.setText(MyUserData.getInstance().getMajor());
        text_hobby.setText(MyUserData.getInstance().getHobby());

        getMyProfile();
    }

    public void setModify(View v){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("정보 공개범위 설정")
                .setMessage("선택 하신대로 설정을 변경 하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              setProfile();
                              finish();
                            }
                        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alert.show();
    }
    public void setProfile(){

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Tag",response.toString());
                } catch (Exception e) {
                    Log.e("Tag", "setProfileResponse error " + e.getMessage());
                    e.printStackTrace();                }
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dateofbirth", rbtn_birth_1.isChecked());
            jsonObject.put("hobby", rbtn_hobby_1.isChecked());
            jsonObject.put("major", rbtn_major_1.isChecked());
            jsonObject.put("residence", rbtn_residence_1.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setProfileRequest request = new setProfileRequest(responseListener,jsonObject);
        RequestQueue queue = Volley.newRequestQueue(ProfileActvity.this);
        queue.add(request);
    }

    class setProfileRequest extends JsonObjectRequest {
        //TODO sns 아이디 확인 주소

        public setProfileRequest(Response.Listener<JSONObject> listener,JSONObject jsonObject) {
            super(Method.POST, "https://capston2webapp.azurewebsites.net/api/UserInfo/" + MyUserData.getInstance().getId(),jsonObject,listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "setProfileRequest error " + error);
                }
            });
        }
        @Override
        public Map<String, String> getParams() {
            HashMap<String, String> parameters = new HashMap<String, String>();
            return parameters;
        }
    }
    public void getMyProfile(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("dateofbirth").equals("null")){
                        rbtn_birth_2.setChecked(true);
                    }else{
                        rbtn_birth_1.setChecked(true);
                    }
                    if(jsonObject.getString("residence").equals("null")){
                        rbtn_residence_2.setChecked(true);
                    }else{
                        rbtn_residence_1.setChecked(true);
                    }
                    if(jsonObject.getString("major").equals("null")){
                        rbtn_major_2.setChecked(true);
                    }else{
                        rbtn_major_1.setChecked(true);
                    }
                    if(jsonObject.getString("hobby").equals("null")){
                        rbtn_hobby_2.setChecked(true);
                    }else{
                        rbtn_hobby_1.setChecked(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/UserInfo/"+ MyUserData.getInstance().getId();
        StringRequest getBoardRequest = new StringRequest(Request.Method.GET,URL,responseListener,null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getBoardRequest);
    }
    class getProfileRequest extends StringRequest {
        //TODO sns 아이디 확인 주소
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/UserInfo";
        private Map<String, String> parameters;

        public getProfileRequest(Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "error " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("id", MyUserData.getInstance().getId());
            Log.d("Tag", "id :  " + MyUserData.getInstance().getId());
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
}
