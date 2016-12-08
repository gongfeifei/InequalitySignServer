package com.example.lenovo.inequalitysignserver.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.adapter.DBAdapter;
import com.example.lenovo.inequalitysignserver.entity.Account;

public class RegisterActivity extends AppCompatActivity {

    private DBAdapter dbAdapter = new DBAdapter(this);
    private EditText mEtName;
    private EditText mEtPwd;
    private Button mBtnRegister;
    private Button mBtnCancel;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.BtnRegister:
                    Toast.makeText(RegisterActivity.this, "注册成功，请填写基本信息！",
                            Toast.LENGTH_SHORT).show();
                    intent.setClass(RegisterActivity.this, ManagerActivity.class);
                    SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("UNAME", mEtName.getText().toString());
                    editor.putString("PWD", mEtPwd.getText().toString());
                    editor.commit();
//                    intent.putExtra("UNAME", mEtName.getText().toString());
//                    intent.putExtra("PWD", mEtPwd.getText().toString());
                    startActivity(intent);
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
