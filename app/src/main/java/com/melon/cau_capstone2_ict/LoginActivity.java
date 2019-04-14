package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

public class LoginActivity extends AppCompatActivity {
    private static final String AUTH_TYPE = "rerequest";

    private EditText login_id;
    private EditText login_pass;
    private AlertDialog dialog;

    private FaceBookLoginResult mFaceBookLoginResult;
    private LoginManager mLoginManager;
    private CallbackManager mCallbackFacebook;
    private SessionCallback mCallbackKakao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = (EditText) findViewById(R.id.signup_id);
        login_pass = (EditText) findViewById(R.id.login_password);

        mFaceBookLoginResult = new FaceBookLoginResult();
        mLoginManager = LoginManager.getInstance();
        mCallbackFacebook = CallbackManager.Factory.create();

        mLoginManager.setAuthType(AUTH_TYPE);
        mLoginManager.registerCallback(mCallbackFacebook, mFaceBookLoginResult);

        mCallbackKakao = new SessionCallback();
        Session.getCurrentSession().addCallback(mCallbackKakao);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackFacebook.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mCallbackKakao);
    }

    public void onClickLogin(View v){
        //  ID/password check , Login

        String id = login_id.getText().toString();
        String pass = login_pass.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String> () {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response : " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    MyUserData.getInstance().setData(jsonResponse.getString("id"),jsonResponse.getString("nickname"));
                    MyUserData.getInstance().setResidence("서울");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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

    public void onClickSNS_kakao(View v){
        Session.getCurrentSession().open(AuthType.KAKAO_TALK, LoginActivity.this);
    }

    public void onClickSNS_facebook(View v){
        mLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
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

    private class FaceBookLoginResult implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            setResult(RESULT_OK);
            String str = loginResult.getAccessToken().getUserId();
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("type", "Facebook");
//            intent.putExtra("id", loginResult.getAccessToken().getUserId());
//            startActivity(intent);
//            finish();
            Log.d("Tag", "facebook ID : " + str);

        }

        @Override
        public void onCancel() {
            setResult(RESULT_CANCELED);
        }

        @Override
        public void onError(FacebookException e) {
            // Handle exception
        }
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.getInstance().requestMe(new MeResponseCallback() {

                @Override
                public void onSuccess(UserProfile result) {
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("type", "Kakao");
//                    intent.putExtra("id", String.valueOf(result.getId()));
//                    startActivity(intent);
//                    finish();
                    Log.d("Tag", "kakao ID : " + String.valueOf(result.getId()));
                    //Todo ID 비교해서 회원이면 로그인, 비회원이면 정보입력으로
                    //validationCheck(String.valueOf(result.getId()));

                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }

                @Override
                public void onNotSignedUp() {
                    Log.d("Tag", "2323 ");

                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login);
        }
    }
    public static String getHashKey(Context context) {

        final String TAG = "KeyHash";

        String keyHash = null;

        try {

            PackageInfo info =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md;

                md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                keyHash = new String(Base64.encode(md.digest(), 0));

                Log.d(TAG, keyHash);

            }

        } catch (Exception e) {

            Log.e("name not found", e.toString());

        }

        if (keyHash != null) {

            return keyHash;

        } else {

            return null;

        }

    }
    public void validationCheck(String memberID){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response : " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isValidate = jsonResponse.getBoolean("return");
                    if (isValidate) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //Todo sns 로그인시 넘길 정보
                        intent.putExtra("Member_Name","SNS TEST");
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, Signup2Activity.class);
                        intent.putExtra("Member_ID","SNS ID");
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoginActivity.ValidateRequest validateRequest = new LoginActivity.ValidateRequest(memberID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(validateRequest);
    }
    class ValidateRequest extends StringRequest {
        //TODO sns 아이디 확인 주소
        final static private String URL = "";
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



