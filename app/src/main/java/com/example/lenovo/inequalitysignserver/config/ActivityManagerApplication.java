package com.example.lenovo.inequalitysignserver.config;

import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2016/12/20.
 */
public class ActivityManagerApplication extends Application {
    private static Map<String, Activity> destoryMap = new HashMap<>();

    public ActivityManagerApplication() {
    }

    /**
     * 添加到销毁队列
     * @param activity  要销毁的activity
     * @param activityName
     */
    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     * @param activityName
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            destoryMap.get(key).finish();
        }
    }

}
