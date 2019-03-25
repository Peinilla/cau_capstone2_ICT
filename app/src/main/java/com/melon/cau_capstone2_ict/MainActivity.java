package com.melon.cau_capstone2_ict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private String ID;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent;
        intent = getIntent();

        ID = intent.getStringExtra("Member_ID");
        name = intent.getStringExtra("Member_Name");
    }
}
