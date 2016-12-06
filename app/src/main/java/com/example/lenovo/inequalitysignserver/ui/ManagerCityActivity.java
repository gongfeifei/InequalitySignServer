package com.example.lenovo.inequalitysignserver.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;

import java.util.ArrayList;

public class ManagerCityActivity extends AppCompatActivity {

    private ImageButton mIBtnBack;
    private ListView mLvProvince;
    private ListView mLvCity;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.IBtnCityBack:
                    finish();
                    break;
            }
        }
    };
    private String[] provinces = new String[] {"安徽", "澳门", "北京", "重庆", "福建", "甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江"};
    private String[][] citys = new String[][] {{"安庆", "蚌埠", "亳州", "巢湖", "池州", "滁州",
            "阜阳", "合肥", "淮北", "淮南", "黄山", "六安", "马鞍山", "宿州", "铜陵", "芜湖",
            "宣城"}, {"澳门"}, {"北京"}, {"重庆", "重庆县"}, {"福州", "龙岩", "南平", "宁德",
            "莆田", "泉州", "厦门", "三明", "漳州"}, {"白银", "定西", "甘南藏族自治州",
            "嘉峪关", "金昌", "酒泉", "兰州", "临夏回族自治州", "陇南", "平凉", "庆阳", "天水",
            "武威", "张掖"}, {"潮州", "东莞", "佛山", "广州", "河源", "惠州", "江门", "揭阳",
            "茂名", "梅州", "清远", "汕头", "汕尾", "韶关", "深圳", "阳江", "云浮", "湛江",
            "肇庆", "中山", "珠海"}, {"百色", "北海", "崇左", "防城港", "桂林", "贵港", "河池",
            "贺州", "来宾", "柳州", "南宁", "钦州", "梧州", "玉林"}, {"安顺", "毕节地区",
            "贵阳", "六盘水", "黔东南苗族侗族自治州", "黔南布衣族苗族自治州",
            "黔西南布衣族苗族自治州", "铜仁地区", "遵义"}, {"海口", "三亚",
            "省直辖县级行政区划"}, {"保定", "沧州", "承德", "邯郸", "衡水", "廊坊", "秦皇岛",
            "石家庄", "唐山", "邢台", "张家口"}, {"安阳", "鹤壁", "焦作", "开封", "洛阳",
            "南阳", "平顶山", "三门峡", "商丘", "新乡", "信阳", "许昌", "郑州", "周口",
            "驻马店", "漯河", "濮阳"}, {"大庆", "大兴安岭地区", "哈尔滨", "鹤岗", "黑河",
            "鸡西", "佳木斯", "牡丹江", "七台河", "齐齐哈尔", "双鸭山", "绥化", "伊春"}};
    private ArrayAdapter<String> mProvinceAdapter;
    private ArrayAdapter<String> mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_city);
        findView();
        setListener();
        mProvinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, provinces);

        mLvProvince.setAdapter(mProvinceAdapter);
    }

    private void setListener() {
        mIBtnBack.setOnClickListener(mOClickListener);
        mLvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mCityAdapter = new ArrayAdapter<String>(ManagerCityActivity.this,
                        android.R.layout.simple_expandable_list_item_1, citys[position]);
                mLvCity.setAdapter(mCityAdapter);
                mLvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                        String city = citys[position][position2];
                        Toast.makeText(ManagerCityActivity.this, city, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void findView() {
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnCityBack);
        mLvProvince = (ListView) findViewById(R.id.LvProvince);
        mLvCity = (ListView) findViewById(R.id.LvCity);
    }
}
