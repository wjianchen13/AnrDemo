package com.example.anrdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.anrdemo.test1.TestActivity1;

public class MainActivity extends AppCompatActivity {

    // 通知权限申请的回调
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化权限申请回调
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "通知权限已授予，卡顿通知将正常显示", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "通知权限被拒绝，卡顿通知可能无法显示", Toast.LENGTH_LONG).show();
                    }
                }
        );

        // 检查并申请通知权限（Android 13+）
        checkAndRequestNotificationPermission();
    }

    /**
     * 检查并申请通知权限
     * Android 13+ (API 33+) 需要运行时申请 POST_NOTIFICATIONS 权限
     */
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ 需要申请通知权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 权限未授予，申请权限
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
        // Android 12 及以下版本不需要申请，通知权限默认授予
    }

    public void onTest1(View v) {
        startActivity(new Intent(this, TestActivity1.class));
    }

}