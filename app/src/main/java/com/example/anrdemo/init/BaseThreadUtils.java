package com.example.anrdemo.init;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 全局线程池工具类
 */
public class BaseThreadUtils {

    // 消息线程池（5 个线程）
    private static final ExecutorService HAYUKI_MESSAGE_EXECUTOR = Executors.newFixedThreadPool(5);

    // 普通线程池（5 个线程）
    private static final ExecutorService HAYUKI_NORMAL_EXECUTOR = Executors.newFixedThreadPool(5);

    // 下载线程池（4 个线程）
    private static final ExecutorService HAYUKI_DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(4);

    /**
     * 获取消息线程池
     */
    public static ExecutorService messageGlobalExecute() {
        return HAYUKI_MESSAGE_EXECUTOR;
    }

    /**
     * 判断是否在主线程
     */
    public static boolean isGlobalMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 获取下载线程池
     */
    public static ExecutorService downloadExecutor() {
        return HAYUKI_DOWNLOAD_EXECUTOR;
    }

    /**
     * 获取当前线程名称
     */
    public static String curGlobalThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 获取普通线程池
     */
    public static ExecutorService normalGlobalExecute() {
        return HAYUKI_NORMAL_EXECUTOR;
    }
}