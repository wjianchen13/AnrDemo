package com.example.anrdemo.init;

import android.content.Context;

/**
 * 启动任务抽象类
 */
public abstract class StartupTask {

    /**
     * 任务名称（用于日志）
     */
    public abstract String getName();

    /**
     * 是否在主线程执行
     * true = 主线程执行
     * false = 子线程执行
     */
    public abstract boolean runOnMainThread();

    /**
     * 执行任务
     */
    public abstract void execute(Context context);
}