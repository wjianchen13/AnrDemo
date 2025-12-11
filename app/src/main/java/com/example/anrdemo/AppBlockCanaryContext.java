package com.example.anrdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class AppBlockCanaryContext extends BlockCanaryContext {

    private Context mContext;

    public AppBlockCanaryContext(Context context) {
        this.mContext = context;
    }

    /**
     * 标识当前应用的版本和渠道信息
     *
     * @return Qualifier which can specify this installation, like version + flavor.
     */
    public String provideQualifier() {
        return "unknown";
    }

    /**
     * 标识当前用户
     * 追踪特定用户的卡顿情况
     * 分析是个别用户问题还是普遍问题
     * 联系用户获取更多信息
     * @return user id
     */
    public String provideUid() {
        return "uid";
    }

    /**
     * Network type
     *
     * @return {@link String} like 2G, 3G, 4G, wifi, etc.
     */
    public String provideNetworkType() {
        ConnectivityManager cm = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return "none";
        }

        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return "wifi";
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4g";
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return "3g";
                    default:
                        return "2g";
                }
            default:
                return "unknown";
        }
    }

    /**
     * 设置监控持续多长时间后自动停止
     *
     * @return monitor last duration (in hour)
     */
    public int provideMonitorDuration() {
        return -1; // -1 表示一直监控
    }

    /**
     * 定义多长时间算"卡顿" 单位毫秒
     *
     * @return threshold in mills
     */
    public int provideBlockThreshold() {
        // 根据设备性能动态设置
        ActivityManager am = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memInfo);

        // 总内存
        long totalMem = memInfo.totalMem / (1024 * 1024); // MB

        if (totalMem < 2048) {
            // 低端机（内存 < 2GB）：阈值 2 秒
            Utils.log("provideBlockThreshold: " + 2000);
            return 2000;
        } else if (totalMem < 4096) {
            // 中端机（内存 2-4GB）：阈值 1 秒
            Utils.log("provideBlockThreshold: " + 1000);
            return 1000;
        } else {
            // 高端机（内存 > 4GB）：阈值 500 毫秒
            Utils.log("provideBlockThreshold: " + 500);
            return 500;
        }
    }

    /**
     * 卡顿发生时，多久采样一次主线程堆栈
     */
    public int provideDumpInterval() {
        return provideBlockThreshold();
    }

    /**
     * 日志保存路径
     *
     * @return path of log files
     */
    public String providePath() {
        // 注意：不要以 / 结尾！
        String path = mContext.getFilesDir().getAbsolutePath();
        Log.d("BlockCanary", "日志路径: " + path);
        return path;
    }

    /**
     * 是否显示通知
     *
     * @return true if need, else if not need.
     */
    public boolean displayNotification() {
        return true; // 启用通知显示
    }

    /**
     * 压缩日志文件
     *
     * @param src  files before compress
     * @param dest files compressed
     * @return true if compression is successful
     */
    public boolean zip(File[] src, File dest) {
        return false;
    }

    /**
     * 将日志上传到服务器
     *
     * @param zippedFile zipped file
     */
    public void upload(File zippedFile) {
        throw new UnsupportedOperationException();
    }


    /**
     *  指定只关注哪些包的代码
     *
     * @return null if simply concern only package with process name.
     */
    public List<String> concernPackages() {
        return null; // null 表示关注当前进程的包
    }

    /**
     * 配合 concernPackages() 使用，决定是否完全删除无关堆栈
     *
     * @return true if filter, false it not.
     */
    public boolean filterNonConcernStack() {
        return false;
    }

    /**
     * 白名单中的代码不会被报告为卡顿
     *
     * @return return null if you don't need white-list filter.
     */
    public List<String> provideWhiteList() {
        LinkedList<String> whiteList = new LinkedList<>();
        whiteList.add("org.chromium");
        return whiteList;
    }

    /**
     *  删除白名单日志
     *
     * @return true if delete, false it not.
     */
    public boolean deleteFilesInWhiteList() {
        return true;
    }

    /**
     * 卡顿发生时的回调，可以自定义处理
     */
    public void onBlock(Context context, BlockInfo blockInfo) {
        // 记录卡顿信息
        Log.e("BlockCanary", "检测到卡顿！");
        Log.e("BlockCanary", "卡顿时长: " + blockInfo.timeCost + "ms");
        Log.e("BlockCanary", "发生时间: " + blockInfo.timeStart);
        
        // BlockCanary 会自动显示通知（因为 displayNotification() 返回 true）
        // 这里可以添加自定义处理逻辑，比如上传到服务器等
    }

}