package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
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
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyBoard;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
import com.melon.cau_capstone2_ict.Manager.OnBackManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_boardWrite extends Fragment implements MainActivity.OnBackPressedListener{

    EditText titleView;
    EditText textView;
    String boardID;
    CheckBox babCheck;

    boolean isGPS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_boardwrite, container, false);
        boardID = getArguments().getString("boardID");
        isGPS = getArguments().getBoolean("isGPS");

        titleView = rootView.findViewById(R.id.board_wtitle);
        textView = rootView.findViewById(R.id.board_wtext);
        babCheck = rootView.findViewById(R.id.board_write_bab);

        ImageButton btn = (ImageButton)rootView.findViewById(R.id.board_back);
        ImageButton write = (ImageButton)rootView.findViewById(R.id.board_write);

        if(!isGPS){
            //babCheck.setVisibility(View.GONE);
        }

        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        write.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String title = titleView.getText().toString();
                String text = textView.getText().toString();

                if(title.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog dialog = builder.setMessage("제목을 입력해주세요.").setNegativeButton("OK", null).create();
                    dialog.show();
                }else if(text.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog dialog = builder.setMessage("본문을 입력해주세요.").setNegativeButton("OK", null).create();
                    dialog.show();
                }else{
                    String msg = "게시글을 작성하시겠습니까?";
                    if(babCheck.isChecked()){
                        msg += "\n밥파티 채팅방이 함께 생성됩니다.";
                    }
                    AlertDialog alert = new AlertDialog.Builder(getActivity())
                            .setTitle("게시글 작성")
                            .setMessage("입력하신 대로 게시글을 작성하시겠습니까?")
                            .setCancelable(true)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            writeRequest(titleView.getText().toString(), textView.getText().toString());
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
            }
        });


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    void setBabParyError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setMessage("밥파티 게시글을 작성할 수 없습니다.\n 2분 후에 다시 시도해주세요.").setNegativeButton("OK", null).create();
        dialog.show();
    }

    void writeRequest(String title,String text){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(babCheck.isChecked()){
                        if(jsonObject.getString("reason").equals("DUPLICATE REQUEST")){
                            setBabParyError();
                        }
                        else {
                            Log.d("Tag", "밥파티 생성");
                            ChatHubManager.getInstance().getHubProxy_bab().invoke("CreateBopChat", MyUserData.getInstance().getId());

                            ChatHubManager.getInstance().getHubProxy_bab().on("onBopChatCreated", new SubscriptionHandler1<String>() {
                                @Override
                                public void run(final String s) {
                                    Log.d("Tag", "밥파티 성공 : " + s);
                                    MyUserData.getInstance().setBop("s");
                                    MyUserData.getInstance().setBabPartyPosting(true);
                                    ChatHubManager.getInstance().getHubProxy_bab().invoke("GetMyBopPartyID", MyUserData.getInstance().getId());
                                    ChatHubManager.getInstance().getHubProxy_bab().on("onMyBopParty", new SubscriptionHandler1<String>() {
                                        @Override
                                        public void run(final String s) {
                                            Log.d("Tag","지금 밥파티 ID : " +s );
                                            MyUserData.getInstance().setBop(s);
                                            ChatHubManager.getInstance().getHubProxy_bab().removeSubscription("onMyBopParty".toLowerCase());
                                        }
                                    }, String.class);
                                    ChatHubManager.getInstance().getHubProxy_bab().removeSubscription("onbopchatcreated");

                                }
                            }, String.class);
                        }
                    }

                    onBack();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
                    Log.e("Tag","write error : " + error);
                }
            });
            String type = "0";
            if(babCheck.isChecked()){
                type = "1";
            }

            parameters = new HashMap<>();
            parameters.put("nickname", MyUserData.getInstance().getNickname());
            parameters.put("text", text);
            parameters.put("title", title);
            parameters.put("residence", boardID);
            parameters.put("type",type);
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        OnBackManager.getInstance().removeOnBackList();
        Object o = OnBackManager.getInstance().getOnBackList();
        activity.setOnBackPressedListener((MainActivity.OnBackPressedListener) o);
        goBack();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Object o = this;
        OnBackManager.getInstance().setOnBackList(o);

        ((MainActivity)context).setOnBackPressedListener(this);
    }
}