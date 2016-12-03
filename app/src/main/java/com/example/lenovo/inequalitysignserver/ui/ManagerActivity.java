package com.example.lenovo.inequalitysignserver.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.inequalitysignserver.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
                    startActivity(intent);
                    break;
                case R.id.LlayManagerType:
                    break;
                case R.id.LlayManagerAddress:
                    intent.setClass(ManagerActivity.this, ManagerAddressActivity.class);
                    startActivity(intent);
                    break;
                case R.id.LlayManagerTel:
                    intent.setClass(ManagerActivity.this, ManagerTelActivity.class);
                    startActivity(intent);
                    break;
                case R.id.IBtnManagerBack:
                    finish();
                    break;
            }
        }
    };
    private ImageButton mIBtnBack;

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.manager_popupwindow, null);
        TextView mTvCamera = (TextView) view.findViewById(R.id.TvPopupCamera);
        TextView mTvPhoto = (TextView) view.findViewById(R.id.TvPopupPhoto);
        TextView mTvCancel = (TextView) view.findViewById(R.id.TvPopupCancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
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
        findView();
        setListener();
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
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    mIvHead.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

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
        mLlayAddress.setOnClickListener(mOClickListener);
        mLlayTel.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mRlayHead = (RelativeLayout) findViewById(R.id.RlayManagerHead);
        mLlayName = (LinearLayout) findViewById(R.id.LlayManagerName);
        mLlayType = (LinearLayout) findViewById(R.id.LlayManagerName);
        mLlayAddress = (LinearLayout) findViewById(R.id.LlayManagerAddress);
        mLlayTel = (LinearLayout) findViewById(R.id.LlayManagerTel);
        mIvHead = (ImageView) findViewById(R.id.IvManagerHead);
        mTvName = (TextView) findViewById(R.id.TvManagerName);
        mTvType = (TextView) findViewById(R.id.TvManagerType);
        mTvAddress = (TextView) findViewById(R.id.TvManagerAddress);
        mTvTel = (TextView) findViewById(R.id.TvManagerTel);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnManagerBack);
    }
}
