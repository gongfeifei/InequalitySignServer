package com.example.lenovo.inequalitysignserver.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
