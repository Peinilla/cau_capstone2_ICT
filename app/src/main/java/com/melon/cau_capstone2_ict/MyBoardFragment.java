package com.melon.cau_capstone2_ict;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.MyBoard;
import com.melon.cau_capstone2_ict.Manager.MyBoardAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
import com.melon.cau_capstone2_ict.Manager.OnBackManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyBoardFragment extends Fragment implements MainActivity.OnBackPressedListener {

    String boardID;
    boolean isGPS = false;

    MyBoardAdapter adapter;
    ListView listView;
    BottomNavigationView bnv;
    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton searchButton;
    FloatingActionButton writeButton;
    ImageButton searchImage;
    ImageButton closeImage;
    ImageButton backImage;
    TextView titleView;
    EditText searchworrd;
    SwipeRefreshLayout mSWL;

    int type_search = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boardID = getArguments().getString("boardID");
        isGPS = getArguments().getBoolean("isGPS");

        final View rootView = inflater.inflate(R.layout.myboard_fragment, container, false);

        titleView = (TextView)rootView.findViewById(R.id.board_title);
        bnv = (BottomNavigationView)rootView.findViewById(R.id.SearchBar) ;
        listView = (ListView) rootView.findViewById(R.id.listview1);
        searchButton = (FloatingActionButton) rootView.findViewById(R.id.button_search);
        writeButton = rootView.findViewById(R.id.button_write);
        searchImage = (ImageButton) rootView.findViewById(R.id.image_search);
        closeImage = (ImageButton) rootView.findViewById(R.id.image_close);
        backImage = rootView.findViewById(R.id.board_back);
        floatingActionsMenu = rootView.findViewById(R.id.board_float);
        mSWL = rootView.findViewById(R.id.board_swipe);
        searchworrd = rootView.findViewById(R.id.board_searchword);
        titleView.setText(boardID + " 게시판");

        if(isGPS){
            backImage.setVisibility(View.VISIBLE);
        }else{
        }

        mSWL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSWL.setRefreshing(false);
                getBoardContext();
            }
        });

        adapter = new MyBoardAdapter();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyBoard m = new MyBoard();
                m = (MyBoard) parent.getItemAtPosition(position);
                Fragment childFragment = new TabFragment_boardView();

                Bundle bundle = new Bundle(1);
                bundle.putSerializable("Board",m);
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.board_container, childFragment).commit();
            }
        });

        searchButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                floatingActionsMenu.collapse();
                bnv.setVisibility(View.VISIBLE);    }
        });

        writeButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                floatingActionsMenu.collapse();
                Fragment childFragment = new TabFragment_boardWrite();
                Bundle bundle = new Bundle(2);
                bundle.putString("boardID",boardID);
                bundle.putBoolean("isGPS",isGPS);
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.board_container, childFragment).commit();
            }
        });

        backImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();         }
        });

        closeImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                bnv.setVisibility(View.GONE);          }
        });
        searchImage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                adapter.search(type_search, searchworrd.getText().toString());
                adapter.reverse();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setSelection(0);
                //Todo 검색
            }
        });

        final Spinner spinner_field = (Spinner) rootView.findViewById(R.id.spinner_search);
        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Tag","search type + " + i);
                type_search = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        String[] str = getResources().getStringArray(R.array.search_item);
        final ArrayAdapter<String> s_adapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str);
        s_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field.setAdapter(s_adapter);

        if(isGPS){
            MainActivity activity = (MainActivity) getActivity();
            Object o = this;
            OnBackManager.getInstance().setOnBackList(o);
            activity.setOnBackPressedListener(this);
        }

        return rootView;
    }
    public void getBoardContext(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    adapter.clear();

                    JSONArray array = new JSONArray(response);
                    for(int inx = 0; inx < array.length(); inx++) {


                        MyBoard m = new MyBoard();
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        if(inx == 0){
                            Log.d("Tag",jsonResponse.toString());
                        }
                        m.setDate(jsonResponse.getString("date"));
                        m.setText(jsonResponse.getString("text"));
                        m.setTitle(jsonResponse.getString("title"));
                        m.setWriter(jsonResponse.getString("nickname"));
                        m.setPostId(jsonResponse.getString("id"));
                        if(jsonResponse.getString("type").equals("1")){
                            m.setBabtype(true);
                        }
                        adapter.addItem(m);
                    }
                    adapter.reverse();
                    adapter.updateDate();
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    listView.setSelection(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "https://capston2webapp.azurewebsites.net/api/ResidencePosts?residenceName="+boardID;
        StringRequest getBoardRequest = new StringRequest(Request.Method.GET,URL,responseListener,null);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getBoardRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        getBoardContext();
    }

    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.gps_board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }
    @Override
    public void onBack() {
        if(isGPS) {
            MainActivity activity = (MainActivity)getActivity();
            OnBackManager.getInstance().removeOnBackList();
            Object o = OnBackManager.getInstance().getOnBackList();
            activity.setOnBackPressedListener((MainActivity.OnBackPressedListener) o);
            goBack();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        super.onDetach();
    }
}