package com.example.anrdemo;

import android.app.Application;
import android.content.Context;

import com.github.moduth.blockcanary.BlockCanary;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在主进程初始化调用哈
        BlockCanary.install(this, new AppBlockCanaryContext(this)).start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
