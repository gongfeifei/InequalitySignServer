package com.example.lenovo.inequalitysignserver.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lenovo.inequalitysignserver.R;

public class ManagerTelActivity extends AppCompatActivity {

    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private EditText mEtTel;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnTelSave:
                    break;
                case R.id.IBtnTelBack:
                    finish();
                    break;
                case R.id.IBtnTelCancel:
                    mEtTel.setText("");
                    break;
            }
        }
    };
    private ImageButton mIBtnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_tel);
        findView();
        setListener();
    }
    private void setListener() {
        mBtnSave.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mBtnSave = (Button) findViewById(R.id.BtnTelSave);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnTelBack);
        mEtTel = (EditText) findViewById(R.id.EtEditTel);
        mIBtnCancel = (ImageButton) findViewById(R.id.IBtnTelCancel);
    }
}
