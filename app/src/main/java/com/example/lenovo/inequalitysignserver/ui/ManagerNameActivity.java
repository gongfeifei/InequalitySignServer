package com.example.lenovo.inequalitysignserver.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.adapter.DBAdapter;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.entity.Account;
import com.example.lenovo.inequalitysignserver.https.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ManagerNameActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtName;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result.equals("1")) {
                Toast.makeText(ManagerNameActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManagerNameActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.BtnNameSave:
                    saveName2Local();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Network network = new Network();
                            NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                            NameValuePair pairName = new BasicNameValuePair("name", name);
                            result = network.sendJsonAndGet(ApiConfig.urlName, pairId, pairName);
                            Log.e("id", ApiConfig.id);
                            Message msg = new Message();
                            h.sendMessage(msg);
                        }
                    }).start();

                    intent.setClass(ManagerNameActivity.this, ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.IBtnNameBack:
                    intent.setClass(ManagerNameActivity.this, ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.IBtnNameCancel:
                    mEtName.setText("");
                    break;
            }
        }
    };
    private DBAdapter dbAdapter;
    private String name;

    private void saveName2Local() {
        name = mEtName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入商家名称", Toast.LENGTH_SHORT).show();
        } else {

            SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("NAME", name);
            editor.commit();


        }

    }

    private ImageButton mIBtnCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_name);
        findView();
        setListener();
        getName();
    }

    private void getName() {
        Intent i = getIntent();
        mEtName.setText(i.getStringExtra("name"));

    }

    private void setListener() {
        mBtnSave.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mBtnSave = (Button) findViewById(R.id.BtnNameSave);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnNameBack);
        mEtName = (EditText) findViewById(R.id.EtEditName);
        mIBtnCancel = (ImageButton) findViewById(R.id.IBtnNameCancel);
    }
}
