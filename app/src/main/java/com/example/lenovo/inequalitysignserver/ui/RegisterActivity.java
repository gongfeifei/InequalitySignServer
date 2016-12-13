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
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.adapter.DBAdapter;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.entity.Account;
import com.example.lenovo.inequalitysignserver.https.Network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class RegisterActivity extends AppCompatActivity {

    private DBAdapter dbAdapter = new DBAdapter(this);
    private EditText mEtName;
    private EditText mEtPwd;
    private Button mBtnRegister;
    private Button mBtnCancel;
    private String result="";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("result", result);
            if (result.equals("exist")) {
                Toast.makeText(RegisterActivity.this, "用户名已存在！", Toast.LENGTH_SHORT).show();
            } else if (result.equals("0")){
                Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "注册成功，请填写基本信息！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, ManagerActivity.class);
                SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString("ID", result);
                editor.putString("UNAME", mEtName.getText().toString());
                editor.putString("PWD", mEtPwd.getText().toString());
                editor.commit();

                startActivity(intent);
                finish();
            }
        }
    };
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.BtnRegister:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Network network = new Network();
                            NameValuePair pairUname = new BasicNameValuePair("shop_id", mEtName.getText().toString());
                            NameValuePair pairPwd = new BasicNameValuePair("shop_pwd", mEtPwd.getText().toString());
                            result = network.sendJsonAndGet(ApiConfig.urlRegister, pairUname, pairPwd);

                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }
                    }).start();



                    break;
                case R.id.BtnRegisterCancel:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        setListener();
    }

    private void setListener() {
        mBtnRegister.setOnClickListener(mOClickListener);
        mBtnCancel.setOnClickListener(mOClickListener);
    }

    private void findView() {

        mEtName = (EditText) findViewById(R.id.EtRegistName);
        mEtPwd = (EditText) findViewById(R.id.EtRegistPwd);
        mBtnRegister = (Button) findViewById(R.id.BtnRegister);
        mBtnCancel = (Button) findViewById(R.id.BtnRegisterCancel);
    }
}
