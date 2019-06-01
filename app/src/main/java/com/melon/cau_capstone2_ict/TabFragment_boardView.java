package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melon.cau_capstone2_ict.Manager.MyBoard;
import com.melon.cau_capstone2_ict.Manager.MyReply;
import com.melon.cau_capstone2_ict.Manager.MyReplyAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TabFragment_boardView extends Fragment implements MainActivity.OnBackPressedListener {

    FrameLayout frameLayout;
    TextView titleView;
    TextView textView;
    TextView writerView;
    TextView dateView;
    View view;
    String writer;

    EditText replyText;
    ListView replyList;
    MyReplyAdapter adapter;

    ImageButton btn_join;
    ImageButton btn_postReply;
    ImageButton btn_back;

    SwipeRefreshLayout mSWL;

    MyBoard m;
    Handler mHandler = new Handler();
    boolean isPosting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_boardview, container, false);

        Bundle bundle = getArguments();
        m = (MyBoard) bundle.getSerializable("Board");

        titleView = rootView.findViewById(R.id.board_title);
        textView = rootView.findViewById(R.id.board_text);
        dateView = rootView.findViewById(R.id.board_date);
        writerView = rootView.findViewById(R.id.board_writer);
        view = rootView.findViewById(R.id.viewProfile);

        replyText = rootView.findViewById(R.id.board_reply_edit);
        btn_join = rootView.findViewById(R.id.button_join);
        btn_back = rootView.findViewById(R.id.board_back);
        btn_postReply = rootView.findViewById(R.id.board_reply_post);

        frameLayout = rootView.findViewById(R.id.board_container);
        replyList = rootView.findViewById(R.id.board_replyList);
        mSWL = rootView.findViewById(R.id.board_reply_swipe);

        adapter = new MyReplyAdapter();

        replyList.setAdapter(adapter);

        if (!m.isBabtype()) {
            btn_join.setVisibility(View.INVISIBLE);
        }

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment childFragment = new TabFragment_chatGroup();
                Bundle bundle = new Bundle(1);
                bundle.putString("GroupID", (String) titleView.getText());
                bundle.putInt("ContainerID",R.id.withbab_container);
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.withbab_container, childFragment).commit();
            }
        });

        titleView.setText(m.getTitle());
        textView.setText(m.getText());
        dateView.setText(m.getDate());
        writer = m.getWriter();
        writerView.setText(writer);

        btn_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), profileViewActivity.class);
                intent.putExtra("id", writer);
                startActivity(intent);
            }
        });
        btn_postReply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPosting) {
                    postReply(replyText.getText().toString());
                }
            }
        });
        mSWL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSWL.setRefreshing(false);
                getReply();
            }
        });


        getReply();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }
    void postReply(String text){
        isPosting = true;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "postReply check : " + response);
                    isPosting = false;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            replyText.setText("");
                            getReply();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/posts/replies/"+ m.getPostId();
        postRequest req = new postRequest(text,URL,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(req);
    }
    class postRequest extends StringRequest {
        private Map<String, String> parameters;

        public postRequest(String text,String URL, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "postRequest error : " + error);
                }
            });
            parameters = new HashMap<>();
            parameters.put("userId", MyUserData.getInstance().getId());
            parameters.put("text", text);
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }
    public void getReply(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag","reply response : " +response);
                    adapter.clear();

                    JSONArray array = new JSONArray(response);
                    for(int inx = 0; inx < array.length(); inx++) {

                        MyReply m = new MyReply();
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        m.setTime(jsonResponse.getString("time"));
                        m.setText(jsonResponse.getString("text"));
                        m.setName(jsonResponse.getString("nickname"));
                        m.setId(jsonResponse.getString("userid"));
                        m.setPostid(jsonResponse.getString("postid"));

                        adapter.addItem(m);
                    }
                    adapter.updateDate();
                    adapter.notifyDataSetChanged();
                    replyList.setAdapter(adapter);
                    replyList.setSelection(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/posts/replies/"+ m.getPostId();
        StringRequest getBoardRequest = new StringRequest(Request.Method.GET,URL,responseListener,null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getBoardRequest);

    }
    void goBack() {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }

    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnBackPressedListener(null);
        goBack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnBackPressedListener(this);
    }
}