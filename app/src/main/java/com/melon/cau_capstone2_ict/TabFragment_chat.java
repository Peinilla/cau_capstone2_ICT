package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.MyChatAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;


public class TabFragment_chat extends Fragment {
    MyChatAdapter adapter;

    FloatingActionsMenu fam;
    FloatingActionsMenu add;
    ListView chatList;

    EditText myMessage;
    ImageButton sendBtn;

    HubProxy hubProxy;
    HubConnection hubConnection;
    Handler mHandler = new Handler();

    ////
    String testSender = "mmmmm";
    ////


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_chat, container, false);



        myMessage = rootView.findViewById(R.id.textView_chat);
        sendBtn = rootView.findViewById(R.id.image_upload);
        add = (FloatingActionsMenu)rootView.findViewById(R.id.chat_add);
        fam = (FloatingActionsMenu)rootView.findViewById(R.id.chat_floatingButton);
        chatList = (ListView) rootView.findViewById(R.id.chat_list);

        adapter = new MyChatAdapter();
//        adapter.addItem(2,"369 하자","물 마시는 토끼");
//        adapter.addItem(2,"너부터","물 마시는 토끼");
//        adapter.addItem(2,"시작","물 마시는 토끼");
//
//        adapter.addItem(0,"1","물 마시는 토끼");
//        adapter.addItem(2,"2","물 마시는 토끼");
//        adapter.addItem(0,"짝","물 마시는 토끼");
//        adapter.addItem(2,"4","물 마시는 토끼");
//        adapter.addItem(2,"중복 채팅 테스트","물 마시는 토끼");
//        adapter.addItem(2,"줄바꿈 테스트 가나다라마바사아자차타파하","물 마시는 토끼");
//        adapter.addItem(0,"test","물 마시는 토끼");

        chatList.setAdapter(adapter);
        add.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fam.expand();
            }

            @Override
            public void onMenuCollapsed() {
                fam.collapse();
            }
        });

        sendBtn.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {


                send();
            }
        });

        connect();
        return  rootView;
    }
    public void send(){
        if (!myMessage.getText().toString().trim().equals("")) { // WebApi Methods
            hubProxy.invoke("sendMessage",MyUserData.getInstance().getId(),testSender,myMessage.getText().toString());//we have parameterized what we want in the web API method
            Log.d("Tag", "send");

        }
    }

    void connect() {
        int inx = 0;
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        Credentials credentials = new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("username", MyUserData.getInstance().getId()); //get username

            }
        };

        String serverUrl="https://capston2webapp.azurewebsites.net/signalr"; // connect to signalr server
        try {
            hubConnection = new HubConnection(serverUrl);
        }catch (Exception e){

        }

        hubConnection.setCredentials(credentials);

        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
            }
        });

        String CLIENT_METHOD_BROADAST_MESSAGE = "getUserList"; // get webapi serv methods
        hubProxy = hubConnection.createHubProxy("chatHub"); // web api  necessary method name
        ClientTransport clientTransport = new ServerSentEventsTransport((hubConnection.getLogger()));
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);
        hubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE, new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try { // we added the list of connected users

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, String.class);
        hubProxy.on("sendMessage", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(final String s, final String s2) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getMessage(s,s2);
                    }
                });

            }
        },String.class,String.class);
        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("SimpleSignalR", e.toString());
            return;
        }

    }
    void disconnect(){ //disconnection server
        hubConnection.stop();
    }

    void getMessage(String s, String s2){
        if(s2.equals(testSender)) {
            adapter.addItem(2, s, s2);
        }else{
            adapter.addItem(0,myMessage.getText().toString().trim(),MyUserData.getInstance().getNickname());
        }
        adapter.notifyDataSetChanged();
        chatList.setAdapter(adapter);
        chatList.setSelection(adapter.getCount() - 1);
    }

    public void onDestroy() {
        disconnect();
        super.onDestroy();
    }

}

