package com.example.lenovo.inequalitysignserver.ui;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.https.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SettingPwdActivity extends AppCompatActivity {

    private ImageButton mIBtnBack;
    private EditText mEtOldpwd;
    private EditText mEtNewpwd;
    private CheckBox mCbShow;
    private Button mBtnSave;
    private String oldPwd = "";
    private String newPwd = "";
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result.equals("1")) {
                Toast.makeText(SettingPwdActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingPwdActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            oldPwd = mEtOldpwd.getText().toString();
            newPwd = mEtNewpwd.getText().toString();
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
                    checkPwd();

                    break;
            }
        }
    };
    private View.OnFocusChangeListener mOFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            oldPwd = mEtOldpwd.getText().toString();
            newPwd = mEtNewpwd.getText().toString();
            switch (v.getId()) {
                case R.id.EtSetOldpwd:
                    if (hasFocus) {

                    } else {
                        if (oldPwd.isEmpty()) {
                            mTvShow1.setText("您还未输入旧密码");
                        } else {
                            mTvShow1.setText("");
                        }
                    }
                    break;
                case R.id.EtSetNewpwd:
                    if (hasFocus) {
                        if (oldPwd.isEmpty()) {
                            mTvShow1.setText("您还未输入旧密码");
                        } else {
                            mTvShow1.setText("");
                        }
                    } else {
                        if (newPwd.isEmpty()) {
                            mTvShow2.setText("请输入密码");
                        } else {
                            mTvShow2.setText("");
                        }
                    }
                    break;
            }
        }
    };

    private void checkPwd() {
        if (oldPwd.isEmpty()) {
            mTvShow1.setText("您还未输入旧密码");
        } else {
            mTvShow1.setText("");
        }
        if (newPwd.isEmpty()) {
            mTvShow2.setText("请输入密码");
        } else {
            mTvShow2.setText("");
        }
        if (oldPwd.equals(ApiConfig.pwd)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Network network = new Network();
                    newPwd = mEtNewpwd.getText().toString();
                    SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                    String id = spf.getString("ID", "");
                    NameValuePair pairId = new BasicNameValuePair("id", id);
                    NameValuePair pairPwd = new BasicNameValuePair("pwd", newPwd);
                    result = network.sendJsonAndGet(ApiConfig.urlPwd, pairId, pairPwd);

                    Message msg = new Message();
                    h.sendMessage(msg);
                }
            }).start();
        } else {
            mTvShow1.setText("旧密码输入错误，请重试");
        }
    }

    private TextView mTvShow1;
    private TextView mTvShow2;

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

        mEtOldpwd.setOnFocusChangeListener(mOFocusChangeListener);
        mEtNewpwd.setOnFocusChangeListener(mOFocusChangeListener);
    }

    private void findView() {
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnPwdBack);
        mEtOldpwd = (EditText) findViewById(R.id.EtSetOldpwd);
        mEtNewpwd = (EditText) findViewById(R.id.EtSetNewpwd);
        mCbShow = (CheckBox) findViewById(R.id.CbPwdShow);
        mBtnSave = (Button) findViewById(R.id.BtnPwdSave);
        mTvShow1 = (TextView) findViewById(R.id.TvSetShow1);
        mTvShow2 = (TextView) findViewById(R.id.TvSetShow2);

    }
}
