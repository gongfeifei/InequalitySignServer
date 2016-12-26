package com.example.lenovo.inequalitysignserver.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.R;
import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.https.Network;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ManagerDescription extends AppCompatActivity {

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //调用照相机返回图片临时文件
    private File tempFile;

    private EditText mEtInform;
    private ImageView mIvPic;
    private Button mBtnSave;
    private ImageButton mIBtnBack;
    private String result = "";
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result.equals("1")) {
                Toast.makeText(ManagerDescription.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManagerDescription.this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Network network;
    private View.OnClickListener mOClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.IvDescriPic:
                    uploadHeadImage();
                    break;
                case R.id.BtnDescriSave:
                    saveDescri2Local();
                    network = new Network();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            NameValuePair pairId = new BasicNameValuePair("id", String.valueOf(ApiConfig.id));
                            NameValuePair pairInform = new BasicNameValuePair("description", inform);
                            result = network.sendJsonAndGet(ApiConfig.urlDescri, pairId, pairInform);
                            Log.e("id", ApiConfig.id);
                            Message msg = new Message();
                            h.sendMessage(msg);
                        }
                    }).start();
                    break;
                case R.id.IBtnDescriBack:
                    Intent intent = new Intent(ManagerDescription.this, ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    private String inform;
    private String cropImagePath;

    private void saveDescri2Local() {
        inform = mEtInform.getText().toString();
        if (inform.isEmpty()) {
            Toast.makeText(this, "请填写描述信息", Toast.LENGTH_SHORT).show();
        } else {

            SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("INFORM", inform);
            editor.commit();

            Intent intent = new Intent(this, ManagerActivity.class);
            startActivity(intent);
            finish();
        }
    }

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
        popupWindow.setOutsideTouchable(false);
        View parent = LayoutInflater.from(this).inflate(R.layout.manager_description, null);
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
        setContentView(R.layout.manager_description);
        createCameraTempFile(savedInstanceState);
        findView();
        setListener();
        getDescri();
    }

    private void getDescri() {
        SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
        String bigimg = spf.getString("BIMG", "");
        String inform = spf.getString("INFORM", "");
        if (!inform.isEmpty()) {
            mEtInform.setText(inform);
        }
        if (!bigimg.isEmpty()) {
            ImageLoader.getInstance().displayImage(bigimg, mIvPic);
        }


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
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    mIvPic.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitMap.compress(Bitmap.CompressFormat.PNG, 100, os);//图片压缩，30代表压缩率，压缩了70%
                    byte[] bigBytes = os.toByteArray();
                    String string = Base64.encodeToString(bigBytes, Base64.DEFAULT);
                    SharedPreferences spf = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("BIMG", string);
                    editor.commit();

                    network.postFile(ManagerDescription.this, cropImagePath, ApiConfig.urlBigimg);

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
        mIvPic.setOnClickListener(mOClickListener);
        mBtnSave.setOnClickListener(mOClickListener);
        mIBtnBack.setOnClickListener(mOClickListener);
    }

    private void findView() {
        mEtInform = (EditText) findViewById(R.id.EtDescriInform);
        mIvPic = (ImageView) findViewById(R.id.IvDescriPic);
        mBtnSave = (Button) findViewById(R.id.BtnDescriSave);
        mIBtnBack = (ImageButton) findViewById(R.id.IBtnDescriBack);
    }
}
