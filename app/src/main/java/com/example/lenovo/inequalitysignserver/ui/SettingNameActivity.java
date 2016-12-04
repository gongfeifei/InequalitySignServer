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

import com.example.lenovo.inequalitysignserver.R;

public class SettingNameActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtUname;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnUnameSave:
                    break;
                case R.id.IBtnUnameBack:
                    finish();
                    break;
                case R.id.IBtnUnameCancel:
                    mEtUname.setText("");
                    break;
            }
        }
    };
    private ImageButton mIBtnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_name);
        findView();
        setListener();
        getUname();
    }

    private void getUname() {
        SharedPreferences spf = getSharedPreferences("Account", Context.MODE_APPEND);
        String name = spf.getString("NAME", "");
        String pwd = spf.getString("PWD", "");
        mEtUname.setText(name);
    }

    private void setListener() {
        mBtnSave.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mBtnSave = (Button) findViewById(R.id.BtnUnameSave);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnUnameBack);
        mEtUname = (EditText) findViewById(R.id.EtSetUname);
        mIBtnCancel = (ImageButton) findViewById(R.id.IBtnUnameCancel);
    }
}
