package com.melon.cau_capstone2_ict;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.MyUserData;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class NfcActivity extends AppCompatActivity {
    TextView view_count;
    TextView nfcText;

    NfcAdapter nfcAdapter;
    NdefMessage ndefMessage;

    Intent intent;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;
    String[][] nfcTechLists;
    String showString;

    String received_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        view_count = findViewById(R.id.nfc_time);
        nfcText = findViewById(R.id.nfc_msg);

        init();
        sendNfc();
    }


    public void init(){ // 초기화
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter.isEnabled()){
            view_count.setText("NFC 작동 중");
        }else{
            view_count.setText("NFC 작동 불가");
            nfcText.setText("휴대폰에서 NFC기능을 켜주세요.");
        }

        intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter ndefIntent = new IntentFilter((NfcAdapter.ACTION_NDEF_DISCOVERED)); // ndef 페이로드 발견시
        try{
            ndefIntent.addDataType("*/*");
            intentFilters = new IntentFilter [] {ndefIntent,}; // intentFilter 변수를 이용해서 intent 필터링
        }
        catch(Exception e)
        {
        }
        nfcTechLists = new String[][] { new String[]{ NfcF.class.getName()}}; // techList 설정
    }

    public void nfcStop(){
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this); // nfc 중지
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter.isEnabled()) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcStop();
    }

    public void getNFC(){
        Intent intent = new Intent(this, profileViewActivity.class);
        intent.putExtra("id",received_msg);
        intent.putExtra("isNFC",true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 인텐트에서 액션을 추출
        String action = intent.getAction();
        // 인텐트에서 태그 정보 추출
        String tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG).toString();
        String strMsg = action + "\n\n" + tag;
        // 액션 정보와 태그 정보를 화면에 출력
        showString = strMsg;

        // 인텐트에서 NDEF 메시지 배열을 구한다
        Parcelable[] messages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages == null) return;

        for(int i=0; i < messages.length; i++) {
            // NDEF 메시지를 화면에 출력
            showMsg((NdefMessage) messages[i]);
        }
        //setPopup(showString);
        getNFC();
    }
    public void showMsg(NdefMessage mMessage) {
        String strMsg = "", strRec="";
        // NDEF 메시지에서 NDEF 레코드 배열을 구한다
        NdefRecord[] recs = mMessage.getRecords();
        for (int i = 0; i < recs.length; i++) {
            // 개별 레코드 데이터를 구한다
            NdefRecord record = recs[i];
            byte[] payload = record.getPayload();
            // 레코드 데이터 종류가 텍스트 일때
            if( Arrays.equals(record.getType(), NdefRecord.RTD_TEXT) ) {
                // 버퍼 데이터를 인코딩 변환
                strRec = byteDecoding(payload);
                received_msg = strRec;
                strRec = "Text: " + strRec;
            }
            // 레코드 데이터 종류가 URI 일때
            else if( Arrays.equals(record.getType(), NdefRecord.RTD_URI) ) {
                strRec = new String(payload, 0, payload.length);
                strRec = "URI: " + strRec;
            }
            strMsg += ("\n\nNdefRecord[" + i + "]:\n" + strRec);
        }

        showString += strMsg;
    }

    // 버퍼 데이터를 디코딩해서 String 으로 변환
    public String byteDecoding(byte[] buf) {
        String strText="";
        String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try {
            strText = new String(buf, langCodeLen + 1,
                    buf.length - langCodeLen - 1, textEncoding);
        } catch(Exception e) {
        }
        return strText;
    }

    public void setNdefMessage(){
        ndefMessage = new NdefMessage( new NdefRecord[]{
                createNewTextRecord(MyUserData.getInstance().getNickname(),Locale.KOREAN,true)
        });
    }

    public static NdefRecord createNewTextRecord(String str, Locale locale, boolean encodelnUtf8){
        // str을 ndef메세지로 반환
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodelnUtf8 ? Charset.forName("UTF-8"):Charset.forName("UTF-16");

        byte[] textBytes = str.getBytes(utfEncoding);

        int utfBit = encodelnUtf8 ? 0:(1<<7);
        char status = (char)(utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte)status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public void sendNfc(){
        setNdefMessage();
        if(nfcAdapter.isEnabled()){
            nfcAdapter.setNdefPushMessage(ndefMessage,this); // nfc 송신
        }
    }
}
