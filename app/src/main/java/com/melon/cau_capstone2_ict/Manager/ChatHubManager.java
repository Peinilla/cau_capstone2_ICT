package com.melon.cau_capstone2_ict.Manager;

import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class ChatHubManager {

    private HubProxy hubProxy;
    private HubConnection hubConnection;
    private boolean isConnect;

    private ArrayList<MyChat> chatArrayList = new ArrayList<>() ;
    private ArrayList<String> currentUser = new ArrayList<>();

    private  static ChatHubManager instance = null;

    private ChatHubManager(){
    }

    public HubProxy getHubProxy(){
        return hubProxy;
    }

    public void send(String to, String message){
        if(!to.equals("") && !message.equals("")) {
            hubProxy.invoke("sendMessage", MyUserData.getInstance().getId(), to, message);//we have parameterized what we want in the web API method
        }
    }

    public void connect() {
        if(isConnect){
            disconnect();
            connect();
            return;
        }
        isConnect = true;

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

        hubProxy = hubConnection.createHubProxy("chatHub"); // web api  necessary method name
        ClientTransport clientTransport = new ServerSentEventsTransport((hubConnection.getLogger()));
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("SimpleSignalR", e.toString());
            return;
        }

    }
    public void disconnect(){ //disconnection server
        isConnect = false;
        hubConnection.stop();
    }

    public ArrayList<String> getCurrentUser(){
        return currentUser;
    }

    public ArrayList<MyChat> getChatArrayList(String to){
        ArrayList<MyChat> m = new ArrayList<>();
        for(int inx = 0; inx < chatArrayList.size(); inx++){
            if(to.equals(chatArrayList.get(inx).getWriter())){
                m.add(chatArrayList.get(inx));
            }
        }
        return m;
    }

    private void getMessage(String s, String s2){
        MyChat m;
        if(s2.equals(MyUserData.getInstance().getId())){
            m = new MyChat(0);

        }else{
            m = new MyChat(2);
        }
        m.setText(s);
        m.setWriter(s2);
        chatArrayList.add(m);
    }

    public static synchronized ChatHubManager getInstance(){
        if(instance == null){
            instance = new ChatHubManager();
        }
        return instance;
    }
}
