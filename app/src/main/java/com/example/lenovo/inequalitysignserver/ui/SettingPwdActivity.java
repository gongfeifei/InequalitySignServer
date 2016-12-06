package com.example.lenovo.inequalitysignserver.ui;

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

import com.example.lenovo.inequalitysignserver.R;

public class SettingPwdActivity extends AppCompatActivity {

    private ImageButton mIBtnBack;
    private EditText mEtOldpwd;
    private EditText mEtNewpwd;
    private CheckBox mCbShow;
    private Button mBtnSave;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.IBtnPwdBack:
                    finish();
                    break;
                case R.id.CbPwdShow:
                    if (mCbShow.isChecked()) {
                        //设置EditText文本为可见的
                        mEtOldpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mEtNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //设置EditText文本为隐藏的
                        mEtOldpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mEtNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    mEtOldpwd.postInvalidate();
                    mEtNewpwd.postInvalidate();
                    //切换后将EditText光标置于末尾
                    CharSequence charSequence = mEtOldpwd.getText();
                    if (charSequence instanceof Spannable) {
                        Spannable spannable = (Spannable) charSequence;
                        Selection.setSelection(spannable, charSequence.length());
                    }
                    CharSequence charSequence2 = mEtNewpwd.getText();
                    if (charSequence2 instanceof Spannable) {
                        Spannable spannable2 = (Spannable) charSequence2;
                        Selection.setSelection(spannable2, charSequence2.length());
                    }
                    break;
                case R.id.BtnPwdSave:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_pwd);
        findView();
        setListener();

    }

    private void setListener() {
        mIBtnBack.setOnClickListener(mOClickListener);
        mBtnSave.setOnClickListener(mOClickListener);
        mCbShow.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnPwdBack);
        mEtOldpwd = (EditText) findViewById(R.id.EtSetOldpwd);
        mEtNewpwd = (EditText) findViewById(R.id.EtSetNewpwd);
        mCbShow = (CheckBox) findViewById(R.id.CbPwdShow);
        mBtnSave = (Button) findViewById(R.id.BtnPwdSave);
    }
}
