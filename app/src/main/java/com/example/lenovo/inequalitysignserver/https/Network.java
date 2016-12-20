package com.example.lenovo.inequalitysignserver.https;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.inequalitysignserver.config.ApiConfig;
import com.example.lenovo.inequalitysignserver.entity.Account;
import com.example.lenovo.inequalitysignserver.entity.Ordertype;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/12.
 */
public class Network {
    private String string = "";
    private List<Account> ls = new ArrayList<>();
    private List<Ordertype> ltype = new ArrayList<>();
    private String uploadStr = "";

    /**
     * 上传数据，返回结果
     * @param u
     * @param pairs
     * @return
     */
    public String sendJsonAndGet(String u, NameValuePair... pairs) {

        URI url = null;
        try {
            url = new URI(u);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            List<NameValuePair> ls = new ArrayList<>();
            for (NameValuePair i : pairs) {
                ls.add(new BasicNameValuePair(i.getName() + "", i.getValue() + ""));
            }
            HttpEntity entity = new UrlEncodedFormEntity(ls, "utf-8");
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                BufferedReader buffer = null;
                buffer = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
                String sb = "";
                while ((sb = buffer.readLine()) != null) {
                    string += sb;
                }
            }
            Log.e("string", string);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return string;
    }



    /**
     * 解析Json数组形式的结果值，返回List列表形式
     * @param s
     * @return
     */
    public List<Account> parserShop(String s) {
        try {
            JSONArray array = new JSONArray(s);
            for(int i =0 ; i < array.length();i++){
                JSONObject object = array.getJSONObject(i);
                String tel = object.getString("shop_tel");
                String city = object.getString("shop_city");
                String inform = object.getString("shop_description");
                String type = object.getString("shop_type");
                String imgsmall = object.getString("shop_img_small");
                String imgbig = object.getString("shop_img_big");
                String name = object.getString("shop_name");
                String adddress = object.getString("shop_address");
                ls.add(new Account(imgsmall, imgbig, name, type, adddress, tel, city, inform));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ls;
    }

    /**
     * 解析Json对象形式的结果值，返回List列表形式
     * @param s
     * @return
     */
    public List<Ordertype> parserType(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String type1 = object.getString("name_type1");
            String type2 = object.getString("name_type2");
            String type3 = object.getString("name_type3");
            Log.e("type", type1 + " " + type2 + " " + type3);
            ltype.add(new Ordertype(type1, type2, type3));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ltype;
    }

    /**
     * 文件上传
     * @param context
     * @param localFile
     */
    public void postFile(final Context context, String localFile) {
        File file = new File(localFile);
        if (file.exists() && file.length() > 0) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            try {
                params.put("id", ApiConfig.id);
                params.put("smallimg_url", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(ApiConfig.urlSmallimg, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Log.e("bytes", new String(bytes));
                    Toast.makeText(context, "更新成功", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(context, "更新失败", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, "图片不支持", Toast.LENGTH_LONG).show();
        }
    }

}