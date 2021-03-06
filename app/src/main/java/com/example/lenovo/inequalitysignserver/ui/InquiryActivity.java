package com.example.lenovo.inequalitysignserver.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lenovo.inequalitysignserver.R;

public class InquiryActivity extends AppCompatActivity {

    private ImageButton mIBtnBack;
    private ListView mLvInquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        findView();
        setListener();
        //号单适配器
    }

    private void setListener() {
        mIBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findView() {
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnInquiryBack);
        mLvInquiry = (ListView) findViewById(R.id.LvInquiry);
    }
}
