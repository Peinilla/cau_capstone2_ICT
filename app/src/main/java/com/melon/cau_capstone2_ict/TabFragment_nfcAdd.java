package com.melon.cau_capstone2_ict;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.ChatHubManager;
import com.melon.cau_capstone2_ict.Manager.MyChatAdapter;
import com.melon.cau_capstone2_ict.Manager.MyUserData;

import java.util.Timer;
import java.util.TimerTask;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;


public class TabFragment_nfcAdd extends Fragment implements MainActivity.OnBackPressedListener {

    TextView view_count;

    Timer timer;
    TimerTask countTask;
    int count = 30;

    NfcAdapter nfcAdapter;
    NdefMessage ndefMessage;
    Intent intent;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;
    String[][] nfcTechLists;
    String showString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_nfcadd, container, false);

        view_count = rootView.findViewById(R.id.nfc_time);
        view_count.setText(String.valueOf(count));

        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if(nfcAdapter.isEnabled()){
            Toast.makeText(getActivity(),"NFC 통신 가능",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(),"NFC 기능을 켜주세요!",Toast.LENGTH_SHORT).show();
        }
        intent = new Intent(getActivity(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        IntentFilter ndefIntent = new IntentFilter((NfcAdapter.ACTION_NDEF_DISCOVERED)); // ndef 페이로드 발견시
        try{
            ndefIntent.addDataType("*/*");
            intentFilters = new IntentFilter [] {ndefIntent,}; // intentFilter 변수를 이용해서 intent 필터링
        }
        catch(Exception e)
        {
        }
        nfcTechLists = new String[][] { new String[]{ NfcF.class.getName()}}; // techList 설정

        // nfc 30s count
        countTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        view_count.setText(String.valueOf(count--));

                        if(count == 0){
                            timer.cancel();
                        }
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(countTask,1000,1000);

        return  rootView;
    }


    void goBack(){
        timer.cancel();

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.profile_container);
        FragmentTransaction tr = fm.beginTransaction();
        tr.remove(fragment);
        tr.commit();
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
        goBack();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter.isEnabled()) {
            nfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilters, nfcTechLists);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(getActivity()); // nfc 중지
        }
    }
}

