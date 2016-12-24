package com.example.lenovo.inequalitysignserver.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lenovo.inequalitysignserver.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class CodeActivity extends AppCompatActivity {

    private ImageButton mBtnBack;
    private ImageView mIvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        findView();
        setListener();
        SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", Context.MODE_APPEND);
        String smallimg = sharedPreferences.getString("SIMG", "");
        Bitmap logo;
        if (!smallimg.isEmpty()) {
            byte []b = Base64.decode(smallimg, Base64.DEFAULT);
            logo = BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        }

        SharedPreferences spf = getSharedPreferences("ACCOUNT", MODE_APPEND);
        String id = spf.getString("ID", "");
        Log.e("code id", id);
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(id, 400, 400,logo);
        mIvCode.setImageBitmap(qrCodeBitmap);
    }

    private void setListener() {
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findView() {
        mBtnBack = (ImageButton) findViewById(R.id.IBtnCodeBack);
        mIvCode = (ImageView) findViewById(R.id.IvCode);
    }
}
