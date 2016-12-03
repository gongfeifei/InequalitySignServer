package com.example.lenovo.inequalitysignserver.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lenovo.inequalitysignserver.R;

public class ManagerNameActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtName;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnNameSave:
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
