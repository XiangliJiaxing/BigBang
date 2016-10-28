package com.xiaoyu.bigbang;

import com.xiaoyu.bigbang.utils.SharedPreferencesUtils;
import com.yolanda.nohttp.NoHttp;

public class Application extends android.app.Application {

    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        NoHttp.initialize(this);
        SharedPreferencesUtils.init(this);
    }

    public static Application getInstance() {
        return _instance;
    }

}