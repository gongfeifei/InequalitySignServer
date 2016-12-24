package com.example.lenovo.inequalitysignserver.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.config.ActivityManagerApplication;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.entity.Ordertype;
import com.example.lenovo.inequalitysignserver.https.Network;
import com.example.lenovo.inequalitysignserver.widget.SatelliteMenu;
import com.example.lenovo.inequalitysignserver.widget.SatelliteMenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SatelliteMenu mSatMenu;
    private Button mBtnClick;
    private Button mBtnCode;
    private ListView mLvType;
    private PopupWindow popupWindow;
    private View parentView;
    private String result = "";
    private List<Ordertype> ltype = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> data = new ArrayList<>();
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {    //获得全部叫号类型
                String type1 = ltype.get(0).getName_type1();
                String type2 = ltype.get(0).getName_type2();
                String type3 = ltype.get(0).getName_type3();
                if (type1.equals("null") && type2.equals("null") && type3.equals("null")) {
                    Toast.makeText(MainActivity.this, "您还没有设置取号类型~", Toast.LENGTH_SHORT).show();
                } else {
                    data.clear();
                    int i = 0;
                    data.add("选择叫号类型");
                    if (!type1.equals("null")) {
                        data.add(++i + "." + type1);
                    }
                    if (!type2.equals("null")) {
                        data.add(++i + "." + type2);
                    }
                    if (!type3.equals("null")) {
                        data.add(++i + "." + type3);
                    }

                    mLvType.setAdapter(adapter);
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
                }
            } else if (msg.what == 1) {     //点击叫号
                popupWindow.dismiss();
                if (result.equals("1")) {
                    Toast.makeText(MainActivity.this, "叫号成功~", Toast.LENGTH_SHORT).show();

                } else if (result.equals("2")) {
                    Toast.makeText(MainActivity.this, "当前没有排队人数~", Toast.LENGTH_SHORT).show();
                } else if ((result.equals("3"))) {
                    Toast.makeText(MainActivity.this, "当前所叫号已取消订单~", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "叫号失败~", Toast.LENGTH_SHORT).show();
                }
            }




        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManagerApplication.addDestoryActivity(this, "MainActivity");

        setContentView(R.layout.activity_main);
        findView();
        setSatMenu();
        setListener();
    }

    private void setSatMenu() {
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(4, R.drawable.setting));
        items.add(new SatelliteMenuItem(3, R.drawable.inquiry));
        items.add(new SatelliteMenuItem(2, R.drawable.type));
        items.add(new SatelliteMenuItem(1, R.drawable.manager));

        mSatMenu.addItems(items);
    }

    private void setListener() {

        mBtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CodeActivity.class);
                startActivity(intent);
            }
        });
        mSatMenu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            @Override
            public void eventOccured(int id) {
                Intent intent = new Intent();
                switch (id) {
                    case 1:

                        intent.setClass(MainActivity.this, ManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, TypeActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this, InquiryActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, SettingActivity.class);
                        startActivityForResult(intent, 10);
                        break;
                }
            }
        });
        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_expandable_list_item_1, data);
                /**
                 * 向服务器发送请求并获得所有的叫号类型
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Network network = new Network();
                        SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                        String id = spf.getString("ID", "");
                        NameValuePair pairId = new BasicNameValuePair("id", id);
                        result = network.sendJsonAndGet(ApiConfig.urlGettype, pairId);
                        ltype = network.parserType(result);
                        Message msg = new Message();
                        msg.what = 0;   //标识获取全部叫号类型的进程
                        h.sendMessage(msg);
                    }
                }).start();

            }
        });
        mLvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position != 0) {
                    //请求并点击叫号
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Network network = new Network();
                            SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                            String id = spf.getString("ID", "");
                            String typenum = String.valueOf(position);
                            NameValuePair pairId = new BasicNameValuePair("id", id);
                            NameValuePair pairTypenum = new BasicNameValuePair("type", typenum);
                            result = network.sendJsonAndGet(ApiConfig.urlClick, pairId, pairTypenum);

                            Message msg = new Message();
                            msg.what = 1;   //标识点击叫号的进程
                            h.sendMessage(msg);
                        }
                    }).start();
                }

            }
        });
    }

    private void findView() {

        mSatMenu = (SatelliteMenu) findViewById(R.id.SatMenuMain);
        mBtnClick = (Button) findViewById(R.id.BtnMainClick);
        mBtnCode = (Button) findViewById(R.id.BtnMainCode);

        View view = LayoutInflater.from(this).inflate(R.layout.click_popupwindow, null);
        mLvType = (ListView) view.findViewById(R.id.LvPopupType);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), android.R.color.transparent, null));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
    }

}
