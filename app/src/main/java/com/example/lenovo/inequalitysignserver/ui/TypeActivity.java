package com.example.lenovo.inequalitysignserver.ui;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.lenovo.inequalitysignserver.R;

import java.util.ArrayList;

public class TypeActivity extends AppCompatActivity {

    private Button mBtnAdd;
    private ImageButton mIBtnBack;
    private EditText mEtType;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnTypeAdd:
                    if (data.get(0) != "默认类型（无）") {
                        data.add(mEtType.getText().toString());
                    } else {
                        data.set(0, mEtType.getText().toString());
                    }

                    mLvType.setAdapter(adapter);
                    break;
                case R.id.IBtnTypeBack:
                    finish();
                    break;
                case R.id.IBtnTypeCancel:
                    mEtType.setText("");
                    break;
            }
        }
    };
    private ImageButton mIBtnCancel;
    private ListView mLvType;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> data = new ArrayList<>();
    private TextView mTvDelete;
    private PopupWindow popupWindow;
    private View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        findView();
        setListener();
        data.add("默认类型（无）");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, data);
        mLvType.setAdapter(adapter);
    }
    private void setListener() {
        mBtnAdd.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
        mLvType.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                //popupWindow在弹窗的时候背景半透明
                final WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });
                mTvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.remove(position);
                        mLvType.setAdapter(adapter);
                        popupWindow.dismiss();
                    }
                });
                return false;
            }
        });

    }

    private void findView() {
        mBtnAdd = (Button) findViewById(R.id.BtnTypeAdd);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnTypeBack);
        mEtType = (EditText) findViewById(R.id.EtEditType);
        mIBtnCancel = (ImageButton) findViewById(R.id.IBtnTypeCancel);
        mLvType = (ListView) findViewById(R.id.LvType);

        View view = LayoutInflater.from(this).inflate(R.layout.type_popwindow, null);
        mTvDelete = (TextView) view.findViewById(R.id.TvPopupDelete);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_type, null);

    }
}
