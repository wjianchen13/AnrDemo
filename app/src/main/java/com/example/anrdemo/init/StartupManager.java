package com.example.anrdemo.init;

import android.content.Context;

import com.example.anrdemo.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 启动管理器（简化版）
 * 只分主线程和子线程两种
 */
public class StartupManager {

    private static final String TAG = "StartupManager";

    // 单例
    private static volatile StartupManager INSTANCE;

    // 任务列表
    private final List<StartupTask> tasks = new ArrayList<>();


    private StartupManager() {
    }

    public static StartupManager getInstance() {
        if (INSTANCE == null) {
            synchronized (StartupManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StartupManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 添加任务
     */
    public StartupManager addTask(StartupTask task) {
        if (task != null) {
            tasks.add(task);
        }
        return this;
    }

    /**
     * 开始启动
     */
    public void start(Context context) {
        long startTime = System.currentTimeMillis();

        log("=======================");
        log("tasks size: " + tasks.size());

        // 分离主线程任务和子线程任务
        List<StartupTask> mainThreadTasks = new ArrayList<>();
        List<StartupTask> asyncTasks = new ArrayList<>();

        for (StartupTask task : tasks) {
            if (task.runOnMainThread()) {
                mainThreadTasks.add(task);
            } else {
                asyncTasks.add(task);
            }
        }

        log("main task: " + mainThreadTasks.size() + " 个");
        log("thread task: " + asyncTasks.size() + " 个");

        // 1. 执行主线程任务（同步）
        for (StartupTask task : mainThreadTasks) {
            executeTask(context, task);
        }

        // 2. 执行子线程任务（异步）
        CountDownLatch latch = new CountDownLatch(asyncTasks.size());
        for (StartupTask task : asyncTasks) {
            BaseThreadUtils.normalGlobalExecute().execute(() -> {
                executeTask(context, task);
                latch.countDown();
            });
        }

        long duration = System.currentTimeMillis() - startTime;
        log("main task complete");
        log("main task duration: " + duration + "ms");

        // 可选：等待子线程任务完成（用于调试）
         waitForAsyncTasks(latch);
    }

    /**
     * 执行单个任务
     */
    private void executeTask(Context context, StartupTask task) {
        long taskStartTime = System.currentTimeMillis();
        String taskName = task.getName();
        String threadName = Thread.currentThread().getName();

        try {
            log("start " + taskName + " - " + threadName);

            task.execute(context);

            long duration = System.currentTimeMillis() - taskStartTime;
            log("complete " + taskName + " - duration: " + duration + "ms");

        } catch (Exception e) {
            if(e != null) {
                e.printStackTrace();
                log("error " + taskName + "\n" + "  e: " + e.getMessage());
            }
        }
    }

    /**
     * 等待异步任务完成（可选，用于测试）
     */
    private void waitForAsyncTasks(CountDownLatch latch) {
        try {
            boolean finished = latch.await(10, TimeUnit.SECONDS);
            if (finished) {
                log("all tasks complete");
            } else {
                log("thread task timeout");
            }
        } catch (InterruptedException e) {
            if(e != null) {
                e.printStackTrace();
                log("wait thread task interrupt  \n e: " + e.getMessage());
            }
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {

    }

    private boolean isLog = true;

    private void log(String str) {
        if(isLog) {
            Utils.log(str);
        }
    }

}