package com.example.lenovo.inequalitysignserver.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.entity.Account;
import com.example.lenovo.inequalitysignserver.entity.Ordertype;
import com.example.lenovo.inequalitysignserver.https.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TypeActivity extends AppCompatActivity {

    private Button mBtnAdd;
    private ImageButton mIBtnBack;
    private EditText mEtType;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {    //添加取号类型
                if (result.equals("1")) {
                    if (data.get(0) != "默认类型（无）") {
                        data.add(mEtType.getText().toString());
                    } else {
                        data.set(0, mEtType.getText().toString());
                    }
                    mLvType.setAdapter(adapter);
                    Toast.makeText(TypeActivity.this, "添加类型成功", Toast.LENGTH_SHORT).show();

                } else if (result.equals("2")) {
                    Toast.makeText(TypeActivity.this, "该取号类型已存在", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TypeActivity.this, "添加失败，最多允许添加3类 =ω=", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 1) {     //获得所有取号类型
                String type1 = ltype.get(0).getName_type1();
                String type2 = ltype.get(0).getName_type2();
                String type3 = ltype.get(0).getName_type3();
                if (!type1.equals("null")) {
                    data.set(0, type1);
                    if (!type2.equals("null")) {
                        data.add(type2);
                    }
                    if (!type3.equals("null")) {
                        data.add(type3);
                    }
                } else {
                    if (!type2.equals("null")) {
                        data.set(0, type2);
                        if (!type3.equals("null")) {
                            data.add(type3);
                        }
                    } else {
                        if (!type3.equals("null")) {
                            data.set(0, type3);
                        }
                    }
                }

                mLvType.setAdapter(adapter);
            } else if (msg.what == 2) {     //删除取号类型
                if (result.equals("1")) {
                    Toast.makeText(TypeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    data.remove(pos);
                    mLvType.setAdapter(adapter);
                } else {
                    Toast.makeText(TypeActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }

        }
    };
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtnTypeAdd:
                    if (mEtType.getText().toString().isEmpty()) {
                        Toast.makeText(TypeActivity.this, "取号类型不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        //请求并添加取号类型
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Network network = new Network();
                                SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                                String id = spf.getString("ID", "");
                                NameValuePair pairId = new BasicNameValuePair("id", id);
                                NameValuePair pairOrdertype = new BasicNameValuePair("type", mEtType.getText().toString());
                                result = network.sendJsonAndGet(ApiConfig.urlOrdertype, pairId, pairOrdertype);

                                Message msg = new Message();
                                msg.what = 0;   //标识添加取号类型的进程
                                h.sendMessage(msg);
                            }
                        }).start();

                    }

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
    private List<Ordertype> ltype = new ArrayList<>();
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        findView();
        setListener();
        getDataFromNetwork();
        data.add("默认类型（无）");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, data);
        mLvType.setAdapter(adapter);
    }
    /**
     * 向服务器发送请求并获得所有的取号类型
     */
    private void getDataFromNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Network network = new Network();
                SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                String id = spf.getString("ID", "");
                NameValuePair pairId = new BasicNameValuePair("id", id);
                result = network.sendJsonAndGet(ApiConfig.urlGettype, pairId);
                ltype = network.parserType(result);
                Log.e("result", result);
                Log.e("size", ltype.size() + "");
                Message msg = new Message();
                msg.what = 1;   //标识获取取号类型的进程
                h.sendMessage(msg);
            }
        }).start();
    }
    private void setListener() {
        mBtnAdd.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mIBtnCancel.setOnClickListener(mOClickListener);
        mLvType.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                pos = position;
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
                        //请求并删除取号类型
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Network network = new Network();
                                SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                                String id = spf.getString("ID", "");
                                NameValuePair pairId = new BasicNameValuePair("id", id);
                                NameValuePair pairDeletetype = new BasicNameValuePair("type", data.get(position));
                                Log.e("delete_type", data.get(position));
                                result = network.sendJsonAndGet(ApiConfig.urlDeletetype, pairId, pairDeletetype);

                                Message msg = new Message();
                                msg.what = 2;   //标识删除取号类型的进程
                                h.sendMessage(msg);
                            }
                        }).start();

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
        popupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), android.R.color.transparent, null));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_type, null);

    }
}
