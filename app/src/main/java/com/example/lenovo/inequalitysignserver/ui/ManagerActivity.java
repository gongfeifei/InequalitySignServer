package com.example.lenovo.inequalitysignserver.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.adapter.DBAdapter;
import com.example.lenovo.inequalitysignserver.config.ActivityManagerApplication;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.config.UILApplication;
import com.example.lenovo.inequalitysignserver.entity.Account;
import com.example.lenovo.inequalitysignserver.https.Network;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //调用照相机返回图片临时文件
    private File tempFile;

    private RelativeLayout mRlayHead;
    private LinearLayout mLlayName;
    private LinearLayout mLlayType;
    private LinearLayout mLlayAddress;
    private LinearLayout mLlayTel;
    //商家图片控件
    private ImageView mIvHead;
    private TextView mTvName;
    private TextView mTvType;
    private TextView mTvAddress;
    private TextView mTvTel;
    private String[] types = new String[]{"美食", "火锅", "海鲜", "自助餐", "冀菜", "鲁菜",
            "川菜", "粤菜", "湘菜", "北京菜", "西北菜", "东北菜", "清真", "烧烤", "韩国料理",
            "中餐", "西餐", "快餐", "甜点"};

    private RadioOnClick radioOnClick = new RadioOnClick(i);
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.RlayManagerHead:
                    uploadHeadImage();
                    break;
                case R.id.LlayManagerName:
                    intent.setClass(ManagerActivity.this, ManagerNameActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.LlayManagerType:
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this)
                            .setTitle("选择商家类型").setSingleChoiceItems(types,
                                    radioOnClick.getIndex(), radioOnClick);
                    builder.create();
                    builder.show();
                    break;
                case R.id.LlayManagerDescription:
                    intent.setClass(ManagerActivity.this, ManagerDescription.class);
                    intent.putExtra("inform", inform);
                    intent.putExtra("bigimg", bigimg);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.LlayManagerCity:
                    intent.setClass(ManagerActivity.this, ManagerCityActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.LlayManagerAddress:
                    intent.setClass(ManagerActivity.this, ManagerAddressActivity.class);
                    intent.putExtra("address", address);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.LlayManagerTel:
                    intent.setClass(ManagerActivity.this, ManagerTelActivity.class);
                    intent.putExtra("tel", tel);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.IBtnManagerBack:
                    finish();
                    break;
                case R.id.BtnManagerSave:   //保存我的商家信息
//                    save2Local();

                    finish();
                    break;
            }
        }
    };
    private String id;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {   //0 商家类型；
                if (result.equals("1")) {
                    Toast.makeText(ManagerActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManagerActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 1) {     //获取基本信息的进程
                smallimg = lshop.get(0).shop_img_small;
                bigimg = lshop.get(0).shop_img_big;
                name = lshop.get(0).shop_name;
                type = lshop.get(0).shop_type;
                inform = lshop.get(0).shop_description;
                city = lshop.get(0).shop_city;
                address = lshop.get(0).shop_address;
                tel = lshop.get(0).shop_tel;

                ImageLoader.getInstance().displayImage(smallimg, mIvHead);
                if (name != null) {
                    mTvName.setText(name);
                }
                if (type != null) {
                    mTvType.setText(type);
                }
                if (inform != null) {
                    mTvDescri.setText(inform);
                }
                if (city != null) {
                    mTvCity.setText(city);
                }
                if (address != null) {
                    mTvAddress.setText(address);
                }
                if (tel != null) {
                    mTvTel.setText(tel);
                }

            }

        }
    };
    private List<Account> lshop = new ArrayList<>();
    private DisplayImageOptions options;

    private void save2Local() {
        Account account = new Account();
        account.shop_id = uname;
        account.shop_pwd = pwd;
        account.shop_img_small = smallimg;
        account.shop_img_big = bigimg;
        account.shop_name = name;
        account.shop_type = type;
        account.shop_address = address;
        account.shop_tel = tel;
        account.shop_city = city;
        account.shop_description = inform;

        long column = dbAdapter.insert(account);
        sharedPreferences.edit().clear().commit();
        Log.e("column", String.valueOf(column));
        if (column == -1) {
            Toast.makeText(ManagerActivity.this, "信息保存失败！",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.e("基本信息", account.toString());
            Toast.makeText(ManagerActivity.this, "信息保存成功！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private DBAdapter dbAdapter;
    private ImageButton mIBtnBack;
    private LinearLayout mLlayCity;
    private TextView mTvCity;
    private LinearLayout mLlayDescription;
    private String uname;
    private Button mBtnSave;
    private String name;
    private String type;
    private String inform;
    private String city;
    private String address;
    private String tel;
    private String pwd;
    private SharedPreferences sharedPreferences;
    private String smallimg;
    private Bitmap bitmap;
    private String bigimg;
    private static int i;
    private TextView mTvDescri;

    /**
     * 上传商家logo
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.manager_popupwindow, null);
        TextView mTvCamera = (TextView) view.findViewById(R.id.TvPopupCamera);
        TextView mTvPhoto = (TextView) view.findViewById(R.id.TvPopupPhoto);
        TextView mTvCancel = (TextView) view.findViewById(R.id.TvPopupCancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), android.R.color.transparent, null));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_manager, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
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

        mTvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到调用系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, REQUEST_CAPTURE);
                popupWindow.dismiss();
            }
        });
        mTvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到调用系统图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
                popupWindow.dismiss();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        createCameraTempFile(savedInstanceState);
        i = 0;
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        findView();
        setListener();
        initImageOptions();
        getAccountId();
//        getDataFromLocal();
        getDataFromNetwork();
    }

    private void initImageOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
    }

    /**
     * 向服务器发送请求并获得商家基本信息
     */
    private void getDataFromNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Network network = new Network();
                NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                result = network.sendJsonAndGet(ApiConfig.urlMessage, pairId);
                lshop = network.parserShop(result);
                SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
                SharedPreferences.Editor editor = spf.edit();
//                String string = Base64.encodeToString(lshop.get(0).shop_img_big, Base64.DEFAULT);
                editor.putString("BIMG", bigimg);
                editor.commit();
                Log.e("id", ApiConfig.id);
                Message msg = new Message();
                msg.what = 1;   //标识获取基本信息的进程
                h.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    private void getAccountId() {
        sharedPreferences = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);

        id = sharedPreferences.getString("ID", "");
        ApiConfig.id = id;
    }
    private void getDataFromLocal() {

        sharedPreferences = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);

        id = sharedPreferences.getString("ID", "");
        ApiConfig.id = id;
        uname = sharedPreferences.getString("UNAME", "");
        pwd = sharedPreferences.getString("PWD", "");
        smallimg = sharedPreferences.getString("SIMG", "");
//        Log.e("+++++++++++++", smallimg);
        name = sharedPreferences.getString("NAME", "");
        type = sharedPreferences.getString("TYPE", "");
        inform = sharedPreferences.getString("INFORM", "");
        bigimg = sharedPreferences.getString("BIMG", "");
        city = sharedPreferences.getString("CITY", "");
        address = sharedPreferences.getString("ADDRESS", "");
        tel = sharedPreferences.getString("TEL", "");

        if (!smallimg.isEmpty()) {
            byte []b = Base64.decode(smallimg, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            mIvHead.setImageBitmap(bitmap);
        }
        mTvName.setText(name);
        mTvType.setText(type);
        mTvDescri.setText(inform);
        mTvCity.setText(city);
        mTvAddress.setText(address);
        mTvTel.setText(tel);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    final String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    mIvHead.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitMap.compress(Bitmap.CompressFormat.PNG, 100, os);//图片压缩，30代表压缩率，压缩了70%
                    byte[] smallBytes = os.toByteArray();
                    final String string = Base64.encodeToString(smallBytes, Base64.DEFAULT);
                    SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("SIMG", string);
                    editor.commit();

                    //请求并修改用户小图标
                    Network network = new Network();
                    network.postFile(ManagerActivity.this, cropImagePath);
                }
                break;
        }
    }
    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    private void setListener() {
        mRlayHead.setOnClickListener(mOClickListener);
        mLlayName.setOnClickListener(mOClickListener);
        mLlayType.setOnClickListener(mOClickListener);
        mLlayDescription.setOnClickListener(mOClickListener);
        mLlayCity.setOnClickListener(mOClickListener);
        mLlayAddress.setOnClickListener(mOClickListener);
        mLlayTel.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
        mBtnSave.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mRlayHead = (RelativeLayout) findViewById(R.id.RlayManagerHead);
        mLlayName = (LinearLayout) findViewById(R.id.LlayManagerName);
        mLlayType = (LinearLayout) findViewById(R.id.LlayManagerType);
        mLlayDescription = (LinearLayout) findViewById(R.id.LlayManagerDescription);
        mLlayCity = (LinearLayout) findViewById(R.id.LlayManagerCity);
        mLlayAddress = (LinearLayout) findViewById(R.id.LlayManagerAddress);
        mLlayTel = (LinearLayout) findViewById(R.id.LlayManagerTel);
        mIvHead = (ImageView) findViewById(R.id.IvManagerHead);
        mTvName = (TextView) findViewById(R.id.TvManagerName);
        mTvType = (TextView) findViewById(R.id.TvManagerType);
        mTvDescri = (TextView) findViewById(R.id.TvManagerDescription);
        mTvCity = (TextView) findViewById(R.id.TvManagerCity);
        mTvAddress = (TextView) findViewById(R.id.TvManagerAddress);
        mTvTel = (TextView) findViewById(R.id.TvManagerTel);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnManagerBack);
        mBtnSave = (Button) findViewById(R.id.BtnManagerSave);
    }

    /**
     * 点击单选框事件
     */
    class RadioOnClick implements DialogInterface.OnClickListener{
        private int index;

        public RadioOnClick(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            setIndex(which);
//            Toast.makeText(ManagerActivity.this, "您选择了：" + index + ":" + types[index],
//                    Toast.LENGTH_SHORT).show();
            i = index;
            mTvType.setText(types[index]);
            SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("TYPE", types[index]);
            editor.commit();

            //请求并修改商家类型
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Network network = new Network();
                    NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                    NameValuePair pairType = new BasicNameValuePair("type", types[index]);
                    result = network.sendJsonAndGet(ApiConfig.urlType, pairId, pairType);
                    Log.e("id", ApiConfig.id);
                    Message msg = new Message();
                    msg.what = 0;   //标识修改商家类型的进程
                    h.sendMessage(msg);
                }
            }).start();

            dialog.dismiss();
        }
    }
}
