package com.example.lenovo.inequalitysignserver.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.widget.SatelliteMenu;
import com.example.lenovo.inequalitysignserver.widget.SatelliteMenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mBtnManager;
    private Button mBtnType;
    private Button mBtnCall;
    private Button mBtnInquiry;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
//                case R.id.BtnMainManage:
//                    intent.setClass(MainActivity.this, ManagerActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.BtnMainType:
//                    intent.setClass(MainActivity.this, TypeActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.BtnMainCall:
//                    intent.setClass(MainActivity.this, CallActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.BtnMainInquiry:
//                    intent.setClass(MainActivity.this, InquiryActivity.class);
//                    startActivity(intent);
//                    break;
            }
        }
    };
    private SatelliteMenu mSatMenu;
    private Button mBtnClick;
    private Button mBtnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setSatMenu();
        setListener();
    }

    private void setSatMenu() {
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(4, R.drawable.setting));
        items.add(new SatelliteMenuItem(3, R.drawable.inquiry));
        items.add(new SatelliteMenuItem(2, R.drawable.type));
        items.add(new SatelliteMenuItem(1, R.drawable.manager));

        mSatMenu.addItems(items);
    }

    private void setListener() {
//        mBtnManager.setOnClickListener(mOClickListener);
        mBtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CodeActivity.class);
                startActivity(intent);
            }
        });
        mSatMenu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            @Override
            public void eventOccured(int id) {
                Intent intent = new Intent();
                switch (id) {
                    case 1:
                        intent.setClass(MainActivity.this, ManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, TypeActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this, InquiryActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void findView() {
//        mBtnManager = (Button) findViewById(R.id.BtnMainManage);
//        mBtnType = (Button) findViewById(R.id.BtnMainType);
//        mBtnCall = (Button) findViewById(R.id.BtnMainCall);
//        mBtnInquiry = (Button) findViewById(R.id.BtnMainInquiry);
        mSatMenu = (SatelliteMenu) findViewById(R.id.SatMenuMain);
        mBtnClick = (Button) findViewById(R.id.BtnMainClick);
        mBtnCode = (Button) findViewById(R.id.BtnMainCode);
    }

}
