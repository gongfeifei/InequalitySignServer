package com.example.lenovo.inequalitysignserver.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lenovo.inequalitysignserver.adapter.DBAdapter;
import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.entity.Account;

public class LoginActivity extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private EditText mEtName;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private CheckBox mCbRemember;
    private ImageButton mIBtnSwitch;
    private boolean isHidden = true;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnLogin:
                    saveAccount();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
//                    Intent intent = new Intent(LoginActivity.this, CheckActivity.class);
//                    intent.putExtra("NAME", mEtName.getText().toString());
//                    intent.putExtra("PWD", mEtPwd.getText().toString());
//                    startActivityForResult(intent, 10);
//
//                    if (mCbRemember.isChecked()) {
//                        Account account = new Account();
//                        account.Name = mEtName.getText().toString();
//                        account.Pwd = mEtPwd.getText().toString();
//                        dbAdapter.insert(account);
//                    }
                    break;
                case R.id.IBtnLoginSwitch:
                    if (isHidden) {
                        //设置EditText文本为可见的
                        mIBtnSwitch.setImageResource(R.drawable.showpwd);
                        mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //设置EditText文本为隐藏的
                        mIBtnSwitch.setImageResource(R.drawable.hidepwd);
                        mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    isHidden = !isHidden;
                    mEtPwd.postInvalidate();
                    //切换后将EditText光标置于末尾
                    CharSequence charSequence = mEtPwd.getText();
                    if (charSequence instanceof Spannable) {
                        Spannable spannable = (Spannable) charSequence;
                        Selection.setSelection(spannable, charSequence.length());
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        setListener();

    }

    private void getAccount() {
        String name = mEtName.getText().toString();
        if (!name.isEmpty()) {
            Account[] accounts = dbAdapter.queryOneData(name);
            if (accounts != null) {
                mEtPwd.setText(accounts[0].Pwd);
            }
        }


    }

    private void saveAccount() {
        SharedPreferences spf = getSharedPreferences("Account", Context.MODE_APPEND);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("NAME", mEtName.getText().toString());
        editor.putString("PWD", mEtPwd.getText().toString());

        editor.commit();
    }
    private void setListener() {
        mBtnLogin.setOnClickListener(mOClickListener);
        mIBtnSwitch.setOnClickListener(mOClickListener);
        mEtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getAccount();
                }
            }
        });
    }

    private void findView() {
        mEtName = (EditText) findViewById(R.id.EtLoginName);
        mEtPwd = (EditText) findViewById(R.id.EtLoginPwd);
        mBtnLogin = (Button) findViewById(R.id.BtnLogin);
        mCbRemember = (CheckBox) findViewById(R.id.CbLoginRemember);
        mIBtnSwitch = (ImageButton) findViewById(R.id.IBtnLoginSwitch);
    }
}
