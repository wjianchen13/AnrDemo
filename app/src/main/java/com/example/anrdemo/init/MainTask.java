package com.example.anrdemo.init;

import android.content.Context;


/**
 * 主线程执行
 */
public class MainTask extends StartupTask {

    @Override
    public String getName() {
        return "MainTask";
    }

    @Override
    public boolean runOnMainThread() {
        return true;  // 主线程执行
    }

    @Override
    public void execute(Context context) {

    }

}