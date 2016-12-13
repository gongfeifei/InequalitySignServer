package com.example.lenovo.inequalitysignserver.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.https.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
                    Intent intent = new Intent(ManagerCityActivity.this, ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    private String[] provinces = new String[] {"安徽", "澳门", "北京", "重庆", "福建", "甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
            "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
            "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
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
            "鸡西", "佳木斯", "牡丹江", "七台河", "齐齐哈尔", "双鸭山", "绥化", "伊春"},{"鄂州",
            "恩施土家族苗族自治州", "黄冈", "黄石", "荆门", "十堰", "随州", "天门", "武汉",
            "仙桃", "咸宁", "襄阳", "孝感", "宜昌"}, {"常德", "长沙", "郴州", "衡阳", "怀化",
            "娄底", "邵阳", "湘潭", "湘西土家族苗族自治州", "益阳", "永州", "岳阳", "张家界",
            "株洲"}, {"白城", "白山", "长春", "吉林", "辽源", "四平", "松原", "通化", "延边"}, {
            "常州", "淮安", "连云港", "南京", "南通", "苏州", "宿迁", "泰州", "无锡", "徐州",
            "盐城", "扬州", "镇江"}, {"抚州", "赣州", "吉安", "景德镇", "九江", "南昌", "萍乡",
            "上饶", "新余", "宜春", "鹰潭"}, {"鞍山", "本溪", "朝阳", "大连", "丹东", "抚顺",
            "阜新", "葫芦岛", "锦州", "辽阳", "盘锦", "沈阳", "铁岭", "营口"}, {"阿拉善盟",
            "巴彦淖尔", "包头", "赤峰", "鄂尔多斯", "呼和浩特", "呼伦贝尔", "通辽", "乌海",
            "乌兰察布", "锡林郭勒盟", "兴安盟"}, {"固原", "石嘴山", "吴忠", "银川", "中卫"}, {
            "果洛藏族自治州", "海北藏族自治州", "海东地区", "海南藏族自治州", "海西蒙古族自治州",
            "黄南藏族自治州", "西宁", "玉树藏族自治州"}, {"滨州", "德州", "东营", "菏泽",
            "济南", "济宁", "莱芜", "聊城", "临沂", "青岛", "日照", "泰安", "威海", "潍坊",
            "烟台", "枣庄", "淄博"}, {"长治", "大同", "晋城", "晋中", "临汾", "吕梁", "朔州",
            "太原", "沂州", "阳泉", "运城"}, {"安康", "宝鸡", "汉中", "商洛", "铜川", "渭南",
            "西安", "咸阳", "延安", "榆林"}, {"上海"}, {"阿坝藏族羌族自治州", "巴中", "成都",
            "达州", "德阳", "甘孜藏族自治州", "广安", "广元", "乐山", "凉山彝族自治州", "眉山",
            "绵阳", "南充", "内江", "攀枝花", "遂宁", "雅安", "宜宾", "资阳", "自贡", "泸州"}, {
            "高雄", "台北", "台南", "台中"}, {"天津"}, {"阿里地区", "昌都地区", "拉萨",
            "林芝地区", "那曲地区", "日喀则地区", "山南地区"}, {"香港"}, {"阿克苏地区",
            "阿勒泰地区", "巴音郭楞蒙古自治州", "博尔塔拉蒙古自治州", "昌吉回族自治州",
            "哈密地区", "和田地区", "喀什地区", "克拉玛依", "克孜勒苏柯尔克孜自治州", "塔城地区",
            "吐鲁番地区", "乌鲁木齐", "伊犁哈萨克自治州", "自治区直辖县级行政区划"}, {"保山",
            "楚雄彝族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "迪庆藏族自治州",
            "红河哈尼族彝族自治州", "昆明", "丽江", "临沧", "怒江傈僳族自治州", "普洱", "曲靖",
            "文山壮族苗族自治州", "西双版纳傣族自治州", "玉溪", "昭通"},{"杭州", "湖州", "嘉兴",
            "金华", "丽水", "宁波", "衢州", "绍兴", "台州", "温州", "舟山"}};
    private ArrayAdapter<String> mProvinceAdapter;
    private ArrayAdapter<String> mCityAdapter;
    private static int pos;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result.equals("1")) {
                Toast.makeText(ManagerCityActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManagerCityActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_city);
        findView();
        setListener();
        mProvinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, provinces);
        mLvProvince.setAdapter(mProvinceAdapter);

        mCityAdapter = new ArrayAdapter<String>(ManagerCityActivity.this,
                android.R.layout.simple_expandable_list_item_1, citys[pos]);
        mLvCity.setAdapter(mCityAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setListener() {
        mIBtnBack.setOnClickListener(mOClickListener);
        mLvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                pos = position;
                mCityAdapter = new ArrayAdapter<String>(ManagerCityActivity.this,
                        android.R.layout.simple_expandable_list_item_1, citys[position]);
                mLvCity.setAdapter(mCityAdapter);

            }
        });
        mLvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String city = citys[pos][position];
                SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString("CITY", city);
                editor.commit();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Network network = new Network();
                        NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                        NameValuePair pairName = new BasicNameValuePair("city", city);
                        result = network.sendJsonAndGet(ApiConfig.urlCity, pairId, pairName);
                        Log.e("id", ApiConfig.id);
                        Message msg = new Message();
                        h.sendMessage(msg);
                    }
                }).start();
                Intent intent = new Intent(ManagerCityActivity.this, ManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void findView() {
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnCityBack);
        mLvProvince = (ListView) findViewById(R.id.LvProvince);
        mLvCity = (ListView) findViewById(R.id.LvCity);
    }
}
