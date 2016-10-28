package com.xiaoyu.bigbang.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xiaoyu.bigbang.activity.SelectTextActivity;
import com.xiaoyu.bigbang.utils.SharedPreferencesUtils;

/**
 * 监听剪贴板 复制事件  Service
 */

public class MyService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {

    public static final String FILTER="com.xiaoyu.bigbang.android.unload";
    public static final String ON_OFF_PRIMARY_CLIP_CHANG = "onPrimaryClipChanged";
    private ClipboardManager myClipboard;
    private Intent flat;
    private MyReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new MyReceiver();
        registerReceiver(receiver,new IntentFilter(FILTER));
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        myClipboard.addPrimaryClipChangedListener(MyService.this);
        flat = new Intent(MyService.this,SelectTextActivity.class);
        SharedPreferencesUtils.setParam(SharedPreferencesUtils.KEY,true);
    }

    @Override
    public void onPrimaryClipChanged() {
        ClipData data = myClipboard.getPrimaryClip();
        ClipData.Item item = data.getItemAt(0);
        flat.putExtra(SelectTextActivity.FLAT,item.getText().toString());
        flat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyService.this.startActivity(flat);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        SharedPreferencesUtils.setParam(SharedPreferencesUtils.KEY,false);
    }

    /**
     * 发送广播 SelectTextActivity 表明已经卸载监听
     */
    private void sendMyBroadcast(){
        Intent intent = new Intent(SelectTextActivity.FILTER);
        sendBroadcast(intent);
    }

    /**
     * 广播处理类
     */
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isflat = intent.getExtras().getBoolean(ON_OFF_PRIMARY_CLIP_CHANG);
            if(isflat){
                //添加 复制Action 监听
                myClipboard.addPrimaryClipChangedListener(MyService.this);
            } else {
                //移除 复制Action 监听
                myClipboard.removePrimaryClipChangedListener(MyService.this);
                sendMyBroadcast();
            }
        }
    }

}
