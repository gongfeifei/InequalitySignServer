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

public class ManagerAddressActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtAddress;
    private ImageButton mIBtnCancel;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result.equals("1")) {
                Toast.makeText(ManagerAddressActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManagerAddressActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnAddressSave:
                    saveAddress2Local();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Network network = new Network();
                            NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                            NameValuePair pairAddress = new BasicNameValuePair("address", address);
                            result = network.sendJsonAndGet(ApiConfig.urlAddress, pairId, pairAddress);
                            Log.e("id", ApiConfig.id);
                            Message msg = new Message();
                            h.sendMessage(msg);
                        }
                    }).start();
                    break;
                case R.id.IBtnAddressBack:
                    Intent intent = new Intent(ManagerAddressActivity.this, ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.IBtnAddressCancel:
                    mEtAddress.setText("");
                    break;
            }
        }
    };
    private String address;

    private void saveAddress2Local() {
        address = mEtAddress.getText().toString();
        if (address.isEmpty()) {
            Toast.makeText(this, "请定位所在地址", Toast.LENGTH_SHORT).show();
        } else {

            SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("ADDRESS", address);
            editor.commit();

            Intent intent = new Intent(this, ManagerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_address);
        findView();
        setListener();
        getAddress();
    }

    private void getAddress() {
        Intent i = getIntent();
        mEtAddress.setText(i.getStringExtra("address"));
    }

    private void setListener() {
        mBtnSave.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mBtnSave = (Button) findViewById(R.id.BtnAddressSave);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnAddressBack);
        mEtAddress = (EditText) findViewById(R.id.EtEditAddress);
        mIBtnCancel = (ImageButton) findViewById(R.id.IBtnAddressCancel);
    }
}
