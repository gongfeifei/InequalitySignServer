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
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.https.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ManagerTelActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtTel;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result.equals("1")) {
                Toast.makeText(ManagerTelActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManagerTelActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnTelSave:
                    saveTelToLocal();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Network network = new Network();
                            NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                            NameValuePair pairName = new BasicNameValuePair("tel", tel);
                            result = network.sendJsonAndGet(ApiConfig.urlTel, pairId, pairName);
                            Log.e("id", ApiConfig.id);
                            Message msg = new Message();
                            h.sendMessage(msg);
                        }
                    }).start();
                    break;
                case R.id.IBtnTelBack:
                    Intent intent = new Intent(ManagerTelActivity.this, ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.IBtnTelCancel:
                    mEtTel.setText("");
                    break;
            }
        }
    };
    private String tel;

    private void saveTelToLocal() {
        tel = mEtTel.getText().toString();
        if (tel.isEmpty()) {
            Toast.makeText(this, "请输入联系方式", Toast.LENGTH_SHORT).show();
        } else {

            SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("TEL", tel);
            editor.commit();

            Intent intent = new Intent(this, ManagerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private ImageButton mIBtnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_tel);
        findView();
        setListener();
        getTel();
    }

    private void getTel() {
        Intent i = getIntent();
        mEtTel.setText(i.getStringExtra("tel"));
    }

    private void setListener() {
        mBtnSave.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mBtnSave = (Button) findViewById(R.id.BtnTelSave);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnTelBack);
        mEtTel = (EditText) findViewById(R.id.EtEditTel);
        mIBtnCancel = (ImageButton) findViewById(R.id.IBtnTelCancel);
    }
}
