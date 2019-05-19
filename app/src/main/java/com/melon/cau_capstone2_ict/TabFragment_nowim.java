package com.melon.cau_capstone2_ict;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyNowim;
import com.melon.cau_capstone2_ict.Manager.MyNowimAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class TabFragment_nowim extends Fragment {


    ImageButton btn_upload;
    EditText tagText;
    ListView tagListView;
    Button btn_chatroom;
    FrameLayout chatContainer;
    WordCloudView wordCloud;

    ArrayList<WordCloud> list = new ArrayList<>();
    static ArrayList<String> tagArray = new ArrayList<>();
    static ArrayList<String> chatroomArray = new ArrayList<>();

    MyNowimAdapter adapter;

    Handler mHandler = new Handler();

    int dpwidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_now, container, false);

        btn_upload = rootView.findViewById(R.id.now_tagupload);
        btn_chatroom = rootView.findViewById(R.id.now_Chatroom);
        tagText = rootView.findViewById(R.id.now_tag);
        tagListView = rootView.findViewById(R.id.now_taglist);
        chatContainer = rootView.findViewById(R.id.now_container);
        chatContainer.bringToFront();
        wordCloud = rootView.findViewById(R.id.now_wordcloud);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        dpwidth = (int)(width/dm.density) - 10;

        list.add(new WordCloud("지금나는",3));
        list.add(new WordCloud("지금나는",2));
        list.add(new WordCloud("지금나는",1));


        wordCloud.setSize(dpwidth,dpwidth);
        wordCloud.setDataSet(list);
        wordCloud.setScale(dpwidth/10,dpwidth/40);
        wordCloud.setColors(ColorTemplate.PASTEL_COLORS);
        wordCloud.notifyDataSetChanged();


        adapter = new MyNowimAdapter();
        tagListView.setAdapter(adapter);


        ChatHubManager.getInstance().getHubProxygroup().on("updateAvailableTags", new SubscriptionHandler1<String>() {
            @Override
            public void run(final String s) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateList(s);
                    }
                });
            }
        }, String.class);
        ChatHubManager.getInstance().getHubProxygroup().on("updateRightNowChatList", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try {
                    Log.d("Tag", "updateChatList" +s);

                    JSONArray jsonArray = new JSONArray(s);
                    chatroomArray.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                    }
                } catch (Exception e) {
                    Log.e("Tag", "error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, String.class);

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyNowim nowim = (MyNowim)parent.getItemAtPosition(position);
                if(nowim.getCount()>3) {
                    Fragment childFragment = new TabFragment_chatGroup();
                    Bundle bundle = new Bundle(1);
                    bundle.putString("GroupID",nowim.getWord());
                    bundle.putInt("ContainerID",R.id.now_container);
                    childFragment.setArguments(bundle);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.now_container, childFragment).commit();
                }else{
                    Toast.makeText(getActivity(), "인원이 충분하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_upload.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String str = tagText.getText().toString();
                final AlertDialog alert = new AlertDialog.Builder(getActivity())
                        .setTitle("지금나는")
                        .setMessage("태그를 \""+ str + "\"로 변경 하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ChatHubManager.getInstance().setTag(str);
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
        });
        //ChatHubManager.getInstance().setTag("지금나는");

        return rootView;
    }

    public void updateList(String s){
        adapter.clear();
        list.clear();
        list.add(new WordCloud("지금나는",3));
        list.add(new WordCloud("지금나는",2));
        list.add(new WordCloud("지금나는",1));

        try {
            JSONArray jsonArray = new JSONArray(s);
            Log.d("Tag", "nowim list " + s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String word = jsonObject.getString("Key");
                int count = jsonObject.getInt("Value");
                if(count != 0 && !word.equals("지금나는")) {
                    adapter.addItem(word, count);
                    list.add(new WordCloud(word,count+3));
                }
            }
            adapter.notifyDataSetChanged();
            tagListView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Tag", "updateList error " + e.getMessage());
            e.printStackTrace();
        }
        wordCloud.setSize(dpwidth,dpwidth);
        wordCloud.setDataSet(list);
        wordCloud.setScale(dpwidth/10,dpwidth/40);
        wordCloud.setColors(ColorTemplate.PASTEL_COLORS);
        wordCloud.notifyDataSetChanged();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


}