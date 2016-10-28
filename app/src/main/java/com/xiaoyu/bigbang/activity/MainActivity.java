package com.xiaoyu.bigbang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaoyu.bigbang.R;
import com.xiaoyu.bigbang.service.MyService;
import com.xiaoyu.bigbang.utils.SharedPreferencesUtils;

/**
 * 开启服务 关闭服务 Activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int btSelectBg = R.color.colorBlue;
    private int btNormalBg = R.color.colorDarkBlue;
    private Button open;
    private Button close;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar();

        open = (Button) findViewById(R.id.open_bt);
        close = (Button) findViewById(R.id.close_bt);
        open.setOnClickListener(MainActivity.this);
        close.setOnClickListener(MainActivity.this);
    }

    /**
     * 初始化 工具栏
     */
    private void initToolBar() {
        //设置app logo
        toolbar.setLogo(R.mipmap.ic_launcher);
        //设置主标题
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        //设置右上角的填充菜单
        toolbar.inflateMenu(R.menu.main);
        //设置右上角的填充菜单点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();

                switch (menuItemId){
                    case R.id.about:
                        showDialog(R.string.about,R.string.MoreAbout);
                        break;
                    case R.id.author:
                        showDialog(R.string.author,R.string.MoreAuthor);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 显示对话框
     * @param titleId
     * @param messageId
     */
    private void showDialog(int titleId,int messageId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId)
                .setMessage(messageId)
                .setNegativeButton(getResources().getString(R.string.sure), null);
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 初始化 按钮
     */
    private void initView() {
        if(isWorked()){
            closeButton();
        } else {
            openButton();
        }
    }

    /**
     * Service 处于关闭状态
     */
    private void openButton(){
        open.setClickable(true);
        open.setBackgroundResource(btNormalBg);
        open.setText("开启");
        close.setClickable(false);
        close.setBackgroundResource(btSelectBg);
        close.setText("已关闭");
    }

    /**
     * Service 处于开启状态
     */
    private void closeButton(){
        close.setClickable(true);
        close.setBackgroundResource(btNormalBg);
        close.setText("关闭");
        open.setClickable(false);
        open.setBackgroundResource(btSelectBg);
        open.setText("已开启");
    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_bt:
                startMyService();
                closeButton();
                break;
            case R.id.close_bt:
                stopMyService();
                openButton();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMyService();
        SharedPreferencesUtils.setParam(SharedPreferencesUtils.KEY,false);
    }

    /**
     * 开启服务
     */
    public void startMyService(){
        startService(new Intent(MainActivity.this,MyService.class));
        showToast(R.string.open_service);
    }

    /**
     * 关闭服务
     */
    public void stopMyService(){
        stopService(new Intent(MainActivity.this,MyService.class));
        showToast(R.string.close_service);
    }

    /**
     * 判断 服务是否开启
     * @return true false
     */
    private boolean isWorked() {
        return (boolean) SharedPreferencesUtils.getParam(SharedPreferencesUtils.KEY,false);
    }

    public void showToast(int resId){
        Toast.makeText(MainActivity.this,resId,Toast.LENGTH_SHORT).show();
    }
}
