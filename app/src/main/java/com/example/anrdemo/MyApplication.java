package com.example.anrdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.anrdemo.init.MainTask;
import com.example.anrdemo.init.StartupManager;
import com.example.anrdemo.init.ThreadTask;
import com.github.moduth.blockcanary.BlockCanary;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        long appStartTime = System.currentTimeMillis();
        super.onCreate();
        // 在主进程初始化调用哈
        BlockCanary.install(this, new AppBlockCanaryContext(this)).start();

        Utils.log("Application onCreate 开始");

        // 配置启动任务
        StartupManager.getInstance()
                .addTask(new MainTask() {
                    @Override
                    public void execute(Context context) {
                        super.execute(context);
                        onMainInit();
                    }
                })
                .addTask(new ThreadTask(){
                    @Override
                    public void execute(Context context) {
                        super.execute(context);
                        onThreadInit();
                    }
                }).start(this);

        long duration = System.currentTimeMillis() - appStartTime;
        Utils.log("Application onCreate 完成，耗时: " + duration + "ms");
    }

    private void onMainInit() {
        onMainTest1();
        onMainTest2();
    }

    private void onMainTest1() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMainTest2() {
        int count = 0;
        for(int i = 0; i < 10000; i ++) {
            count ++;
        }
        Utils.log("onMainTest2 count: " + count);
    }

    private void onThreadInit() {
        onThreadTest1();
        onThreadTest2();
    }

    private void onThreadTest1() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onThreadTest2() {
        int count = 0;
        for(int i = 0; i < 20000; i ++) {
            count ++;
        }
        Utils.log("onThreadTest2 count: " + count);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 关闭线程池
        StartupManager.getInstance().shutdown();
    }

}
