package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melon.cau_capstone2_ict.Manager.MyBoard;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TabFragment_boardWrite extends Fragment implements MainActivity.OnBackPressedListener{

    EditText titleView;
    EditText textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_boardwrite, container, false);

        titleView = rootView.findViewById(R.id.board_wtitle);
        textView = rootView.findViewById(R.id.board_wtext);

        ImageButton btn = (ImageButton)rootView.findViewById(R.id.board_back);
        ImageButton write = (ImageButton)rootView.findViewById(R.id.board_write);


        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
            });
        write.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                writeRequest();
            }
        });


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }

    void writeRequest(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String title = titleView.getText().toString();
        String text = textView.getText().toString();
        writeRequest wr = new writeRequest(title,text,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(wr);
    }
    class writeRequest extends StringRequest {
        final static private String URL = "https://capston2webapp.azurewebsites.net/api/ResidencePosts";
        private Map<String, String> parameters;

        public writeRequest(String title, String text, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "error " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("nickname", MyUserData.getInstance().getNickname());
            parameters.put("text", text);
            parameters.put("title", title);
            parameters.put("residence", MyUserData.getInstance().getResidence());
            Log.d("Tag", "re " + title);


        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

    @Override
    public void onBack() {
        Log.e("Tag", "onBack()");
        // 리스너를 설정하기 위해 Activity 를 받아옵니다.
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);
        // MainFragment 로 교체
        goBack();


    }
    @Override
    //                     혹시 Context 로 안되시는분은 Activity 로 바꿔보시기 바랍니다.
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }



}