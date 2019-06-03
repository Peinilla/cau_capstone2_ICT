package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String AUTH_TYPE = "rerequest";

    private EditText login_id;
    private EditText login_pass;
    private ImageView imageView;
    private FrameLayout frameLayout;
    private AnimationDrawable animationDrawable;

    private AlertDialog dialog;

    private FaceBookLoginResult mFaceBookLoginResult;
    private LoginManager mLoginManager;
    private CallbackManager mCallbackFacebook;
    private SessionCallback mCallbackKakao;

    private boolean isProceeding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = (EditText) findViewById(R.id.signup_id);
        login_pass = (EditText) findViewById(R.id.login_password);

        frameLayout = findViewById(R.id.image_frame);
        imageView = findViewById(R.id.imageView_splash);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();

        mFaceBookLoginResult = new FaceBookLoginResult();
        mLoginManager = LoginManager.getInstance();
        mCallbackFacebook = CallbackManager.Factory.create();

        mLoginManager.setAuthType(AUTH_TYPE);
        mLoginManager.registerCallback(mCallbackFacebook, mFaceBookLoginResult);

        mCallbackKakao = new SessionCallback();
        Session.getCurrentSession().addCallback(mCallbackKakao);

        isProceeding = false;
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
        String id = login_id.getText().toString();
        String pass = login_pass.getText().toString();

        if(!isProceeding) {
            loginAction(id, pass);
        }
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

    public void loginAction(String id, String password){

        frameLayout.setVisibility(View.VISIBLE);
        animationDrawable.start();

        Response.Listener<String> responseListener = new Response.Listener<String> () {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "login " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    MyUserData.getInstance().setData(jsonResponse.getString("id"),jsonResponse.getString("nickname"));
                    MyUserData.getInstance().setResidence(jsonResponse.getString("residence"));
                    MyUserData.getInstance().setBirth(jsonResponse.getString("dateofbirth"));
                    MyUserData.getInstance().setEmail(jsonResponse.getString("pk_email"));
                    MyUserData.getInstance().setHobby(jsonResponse.getString("hobby"));
                    MyUserData.getInstance().setMajor(jsonResponse.getString("major"));

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
        };
        isProceeding = true;
        LoginRequest loginRequest = new LoginRequest(id, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

    }

    class LoginRequest extends StringRequest {
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/Login";
        private Map<String, String> parameters;

        public LoginRequest(String id, String pw, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isProceeding = false;
                    frameLayout.setVisibility(View.GONE);
                    animationDrawable.stop();
                    Log.d("Tag", "error " + error);
                }
            }); // 해당 정보를 POST 방식으로 URL에 전송
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
                    String kakao_id = "kakao_" + String.valueOf(result.getId());

                    //Todo ID 비교해서 회원이면 로그인, 비회원이면 정보입력으로
                    validationCheck(kakao_id);

                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }

                @Override
                public void onNotSignedUp() {
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
    public void validationCheck(final String memberID){
       Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isValidate = jsonResponse.getBoolean("return");
                    if (!isValidate) {
                        if(!isProceeding) {
                            loginAction(memberID, memberID);
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this, Signup2Activity.class);
                        intent.putExtra("Member_ID",memberID);
                        intent.putExtra("Member_pass",memberID);

                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("Tag", "sns id check error " + e.getMessage());
                    e.printStackTrace();                }
            }
        };

        ValidateRequest validateRequest = new LoginActivity.ValidateRequest(memberID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        validateRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(validateRequest);
    }
    class ValidateRequest extends StringRequest {
        //TODO sns 아이디 확인 주소
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




