package com.example.lenovo.inequalitysignserver.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;

public class ManagerAddressActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtAddress;
    private ImageButton mIBtnCancel;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnAddressSave:
                    saveAddress();
                    break;
                case R.id.IBtnAddressBack:
                    finish();
                    break;
                case R.id.IBtnAddressCancel:
                    mEtAddress.setText("");
                    break;
            }
        }
    };

    private void saveAddress() {
        String address = mEtAddress.getText().toString();
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
