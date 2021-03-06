package com.example.lenovo.inequalitysignserver.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.config.ActivityManagerApplication;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.config.UILApplication;

public class SettingActivity extends AppCompatActivity {

    private ImageButton mIBtnBack;
    private LinearLayout mLlayName;
    private LinearLayout mLlayPwd;
    private Button mBtnExit;
    private LinearLayout mLlayAbout;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.IBtnSettingBack:
                    finish();
                    break;
                case R.id.LlaySettingName:
                    Toast.makeText(SettingActivity.this, "", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.LlaySettingPwd:
                    intent.setClass(SettingActivity.this, SettingPwdActivity.class);
                    startActivity(intent);
                    break;
                case R.id.BtnSettingExit:
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("温馨提示");
                    builder.setMessage("您确定要退出当前应用程序吗？");
                    builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent.setClass(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            UILApplication.flag = 0;
                            ActivityManagerApplication.destoryActivity("MainActivity");
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create();
                    builder.show();
                    break;
                case R.id.LlaySettingAbout:
                    intent.setClass(SettingActivity.this, SettingAboutActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            finishActivity(10);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findView();
        setListener();
    }

    private void setListener() {
        mIBtnBack.setOnClickListener(mOClickListener);
        mLlayName.setOnClickListener(mOClickListener);
        mLlayPwd.setOnClickListener(mOClickListener);
        mBtnExit.setOnClickListener(mOClickListener);
        mLlayAbout.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnSettingBack);
        mLlayName = (LinearLayout) findViewById(R.id.LlaySettingName);
        mLlayPwd = (LinearLayout) findViewById(R.id.LlaySettingPwd);
        mBtnExit = (Button) findViewById(R.id.BtnSettingExit);
        mLlayAbout = (LinearLayout) findViewById(R.id.LlaySettingAbout);
    }
}
