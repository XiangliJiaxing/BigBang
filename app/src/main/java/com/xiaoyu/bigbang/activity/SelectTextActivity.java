package com.xiaoyu.bigbang.activity;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.xiaoyu.bigbang.R;
import com.xiaoyu.bigbang.service.MyService;
import com.xiaoyu.bigbang.utils.NetHelper;
import com.xiaoyu.bigbang.widget.LineWrapLayout;
import com.xiaoyu.bigbang.widget.LoadDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.xiaoyu.bigbang.R.string.none_select;
import static com.xiaoyu.bigbang.service.MyService.ON_OFF_PRIMARY_CLIP_CHANG;

/**
 * 得到剪贴板数据 网络获取数据 展示数据 Activity
 */

public class SelectTextActivity extends AppCompatActivity {

    public static final String FLAT = "text";
    public static final String FILTER="com.xiaoyu.bigbang.android.load";
    private String text;
    private ClipboardManager myClipboard;
    private LineWrapLayout mLineWrapLayout;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_text);
        receiver = new SelectTextActivity.MyReceiver();
        registerReceiver(receiver,new IntentFilter(FILTER));
        initView();
        initData();
    }

    /**
     * 网络连接获取数据
     */
    private void initData() {
        String text = getIntent().getExtras().getString(FLAT);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(NetHelper.MAINURL, RequestMethod.GET);
        request.add("debug", "0");
        request.add("precise", "0");
        request.add("text", text);
        request.add("showapi_appid", getString(R.string.appid));
        request.add("showapi_sign", getString(R.string.appsign));
        NetHelper.getInstance().getRequestQueue().add(0x0, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int i) {
                LoadDialog.show(SelectTextActivity.this);
            }

            @Override
            public void onSucceed(int i, Response<JSONObject> response) {
                JSONArray array = null;
                try {
                    array = response.get().getJSONObject("showapi_res_body").getJSONArray("list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (array != null) {
                    List<String> mData = NetHelper.getInstance().getGson().fromJson(array.toString(), new TypeToken<List<String>>() {
                    }.getType());
                    mLineWrapLayout.setData(mData);
                }
            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                LoadDialog.dismiss(SelectTextActivity.this);
                Toast.makeText(SelectTextActivity.this, R.string.network_fault, Toast.LENGTH_SHORT).show();
                showToast(none_select);
            }

            @Override
            public void onFinish(int i) {
                LoadDialog.dismiss(SelectTextActivity.this);
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mLineWrapLayout = (LineWrapLayout) findViewById(R.id.linewraplayout);
    }

    /**
     * 显示消息
     *
     * @param resId 信息ID
     */
    public void showToast(int resId) {
        Toast.makeText(SelectTextActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    private float x1,x2,y1,y2;
    private float MaxLength = 50;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
            if (y2 - y1 > MaxLength) {
                action();
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 向下划
     */
    public void action(){
        text = mLineWrapLayout.getData();
        if(TextUtils.isEmpty(text)){
            showToast(R.string.none_select);
            finish();
        } else {
            sendMyBroadcast(false);
            LoadDialog.show(SelectTextActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 通过 发广播 让Service绑定和解绑 addPrimaryClipChangedListener
     * @param isON
     */
    private void sendMyBroadcast(boolean isON){
        Intent intent = new Intent(MyService.FILTER);
        intent.putExtra(ON_OFF_PRIMARY_CLIP_CHANG,isON);
        sendBroadcast(intent);
    }

    /**
     * 广播处理类
     */
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            myClipboard.setPrimaryClip(ClipData.newPlainText(FLAT, text));
            showToast(R.string.already_copy);
            LoadDialog.show(SelectTextActivity.this);
            sendMyBroadcast(true);
            finish();
        }
    }

}
