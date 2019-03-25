package com.example.walker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.MapView;
import com.example.walker.R;
import com.example.walker.trace.base.BaseActivity;

public class SplashActivity extends AppCompatActivity {

    private boolean isFirstUse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences("isFirstUse", MODE_WORLD_READABLE);
        isFirstUse = preferences.getBoolean("isFirstUse", true);


        if (isFirstUse) {
            startActivity(new Intent(getApplicationContext(), GuideActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        finish();
        //实例化Editor对象
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putBoolean("isFirstUse", false);
        //提交修改
        editor.commit();
    }


}
