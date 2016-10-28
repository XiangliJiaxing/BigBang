package com.xiaoyu.bigbang.utils;

import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * 网络工具
 */
public class NetHelper {
    public static final String MAINURL ="https://route.showapi.com/269-1";
    /**
     * 请求队列.
     */
    private RequestQueue requestQueue;

    private static NetHelper netHelper;

    private Gson gson;

    public static NetHelper getInstance(){
        if(netHelper == null){
            synchronized (NetHelper.class){
                if(netHelper == null){
                    netHelper = new NetHelper();
                }
            }
        }
        return netHelper;
    }
    public NetHelper(){
        this.requestQueue = NoHttp.newRequestQueue();
        this.gson = new Gson();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public Gson getGson() {
        return gson;
    }
}