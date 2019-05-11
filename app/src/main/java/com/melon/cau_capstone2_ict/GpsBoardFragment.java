package com.melon.cau_capstone2_ict;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.melon.cau_capstone2_ict.Manager.MyBoard;
import com.melon.cau_capstone2_ict.Manager.MyBoardAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;
import com.melon.cau_capstone2_ict.Manager.RSSHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class GpsBoardFragment extends Fragment {

    static String boardID;

    MyBoardAdapter adapter;
    ListView listView;
    BottomNavigationView bnv;
    FloatingActionButton searchButton;
    FloatingActionButton writeButton;
    ImageButton searchImage;
    ImageButton closeImage;
    ImageButton backImage;
    TextView titleView;
    SwipeRefreshLayout mSWL;
    FrameLayout frameLayout;
//    // test
//    ImageView weatherImage;
//    TextView tempText;
//    TextView popText;
//    TextView ptyText;
//    TextView wfText;
//    LinearLayout linearLayout;
//    private String finalUrl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159060500";
//    private RSSHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boardID = getArguments().getString("boardID");
        final View rootView = inflater.inflate(R.layout.gpsboard_fragment, container, false);

        titleView = (TextView)rootView.findViewById(R.id.board_title);
        bnv = (BottomNavigationView)rootView.findViewById(R.id.SearchBar) ;
        listView = (ListView) rootView.findViewById(R.id.listview1);
        searchButton = (FloatingActionButton) rootView.findViewById(R.id.button_search);
        writeButton = rootView.findViewById(R.id.button_write);
        backImage = rootView.findViewById(R.id.gps_back);
        searchImage = (ImageButton) rootView.findViewById(R.id.image_search);
        closeImage = (ImageButton) rootView.findViewById(R.id.image_close);
        mSWL = rootView.findViewById(R.id.board_swipe);
        frameLayout = rootView.findViewById(R.id.board_container);

        titleView.setText(boardID + " 게시판");

        mSWL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSWL.setRefreshing(false);
                adapter.clear();
                getBoardContext();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setSelection(0);
            }
        });

        adapter = new MyBoardAdapter();

        getBoardContext();

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
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        searchButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                bnv.setVisibility(View.VISIBLE);    }
        });

        writeButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment childFragment = new TabFragment_boardWrite();
                Bundle bundle = new Bundle(1);
                bundle.putString("boardID",boardID);
                childFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.board_container, childFragment).commit();
                frameLayout.setVisibility(View.VISIBLE);
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

        final Spinner spinner_field = (Spinner) rootView.findViewById(R.id.spinner_search);

//        // test
//        weatherImage = (ImageView) rootView.findViewById(R.id.imageView_wfKor);
//        wfText = (TextView) rootView.findViewById(R.id.textView_wfKor);
//        tempText = (TextView) rootView.findViewById(R.id.textView_temp);
//        ptyText = (TextView) rootView.findViewById(R.id.textView_pty);
//        popText = (TextView) rootView.findViewById(R.id.textView_pop);
//        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_pty);
//        Log.d("Tag", "1");
//        handler = new RSSHandler(finalUrl);
//        Log.d("Tag", "11");
//        handler.fetchXML();
//        Log.d("Tag", "111");
//        while (handler.parsingComplete) ;

        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Todo 제목/글쓴이 검색 선택 구현해야됨
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String[] str = getResources().getStringArray(R.array.search_item);
        final ArrayAdapter<String> s_adapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str);
        s_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field.setAdapter(s_adapter);

        return rootView;
    }
    public void getBoardContext(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);
                    JSONArray array = new JSONArray(response);
                    for(int inx = 0; inx < array.length(); inx++) {
                        MyBoard m = new MyBoard();
                        JSONObject jsonResponse = array.getJSONObject(inx);
                        m.setDate(jsonResponse.getString("date"));
                        m.setText(jsonResponse.getString("text"));
                        m.setTitle(jsonResponse.getString("title"));
                        m.setWriter(jsonResponse.getString("nickname"));
                        adapter.addItem(m);
                    }


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

    //        weatherParse();
    void goBack(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.gps_board_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }

//    // test
//    public void weatherParse() {
//
//        Log.d("Tag", "12");
//        int hour = handler.getHour();
//        String wfKor = handler.getWfKor();
//        wfText.setText(wfKor);
//        tempText.setText(handler.getTemp());
//        popText.setText(handler.getPop());
//        ptyText.setText(handler.getPty());
//
//        if(handler.getPop().equals("0"))
//            linearLayout.setVisibility(View.GONE);
//        else
//            linearLayout.setVisibility(View.VISIBLE);
//
//        switch (wfKor) {
//            case "맑음":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db01_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db01_n_b);
//                break;
//            case "구름 조금":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db02_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db02_n_b);
//                break;
//            case "구름 많음":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db03_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db03_n_b);
//                break;
//            case "흐림":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db04_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db04_n_b);
//                break;
//            case "비":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db05_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db05_n_b);
//                break;
//            case "눈/비":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db06_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db06_n_b);
//                break;
//            case "눈":
//                if (hour >= 6 && hour < 18)
//                    weatherImage.setImageResource(R.drawable.db08_b);
//                else
//                    weatherImage.setImageResource(R.drawable.db08_n_b);
//                break;
//        }
//
//        Log.d("Tag", "2");
//    }
}