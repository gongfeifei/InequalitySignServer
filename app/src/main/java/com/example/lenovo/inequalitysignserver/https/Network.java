package com.example.lenovo.inequalitysignserver.https;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
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

}