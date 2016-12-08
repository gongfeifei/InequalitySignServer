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
import com.example.lenovo.inequalitysignserver.adapter.DBAdapter;
import com.example.lenovo.inequalitysignserver.entity.Account;

public class ManagerNameActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtName;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnNameSave:
                    saveName();
                    break;
                case R.id.IBtnNameBack:
                    finish();
                    break;
                case R.id.IBtnNameCancel:
                    mEtName.setText("");
                    break;
            }
        }
    };
    private DBAdapter dbAdapter;

    private void saveName() {
        String name = mEtName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入商家名称", Toast.LENGTH_SHORT).show();
        } else {
//            Intent i = getIntent();
//            String uname = i.getStringExtra("UNAME");
//            String pwd = i.getStringExtra("PWD");

            SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("NAME", name);
            editor.commit();

            Intent intent = new Intent(this, ManagerActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private ImageButton mIBtnCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_name);
        findView();
        setListener();
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
