package com.example.anrdemo.init;

import android.content.Context;
import android.util.Log;


/**
 * 子线程执行
 */
public class ThreadTask extends StartupTask {

    @Override
    public String getName() {
        return "ThreadTask";
    }

    @Override
    public boolean runOnMainThread() {
        return false;  // 子线程执行
    }

    @Override
    public void execute(Context context) {

    }

}