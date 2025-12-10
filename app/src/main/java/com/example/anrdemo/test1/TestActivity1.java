package com.example.anrdemo.test1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.anrdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 各测试方法的预期卡顿时间：
 *
 * 测试方法	预期卡顿时长	触发条件	说明
 * 简单卡顿	~1 秒	100%	直接 sleep
 * 中等卡顿	2-3 秒	100%	CPU 密集计算
 * 严重卡顿	5 秒	100%	ANR 级别
 * 循环卡顿	3 秒	100%	死循环模拟
 * IO 卡顿	1-3 秒	取决于设备	文件读写
 * 网络卡顿	1-5 秒	需要网络	同步网络请求
 * 内存分配	1-2 秒	100%	大量对象创建
 * 数据库卡顿	2-4 秒	100%	大量 SQL 操作
 * JSON 解析	1-3 秒	100%	大量数据解析
 * Bitmap 处理	2-5 秒	100%	图片处理
 */
public class TestActivity1 extends AppCompatActivity {

    private static final String TAG = TestActivity1.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupTestButtons();
    }

    public void onTest1(View v) {
        for(int i = 0; i < 100000; i ++) {

        }
    }

    private void setupTestButtons() {
        // 简单卡顿测试
        findViewById(R.id.btn_simple_block).setOnClickListener(v ->
                testSimpleBlock()
        );

        // 中等卡顿测试
        findViewById(R.id.btn_medium_block).setOnClickListener(v ->
                testMediumBlock()
        );

        // 严重卡顿测试
        findViewById(R.id.btn_severe_block).setOnClickListener(v ->
                testSevereBlock()
        );

        // 循环卡顿测试
        findViewById(R.id.btn_loop_block).setOnClickListener(v ->
                testLoopBlock()
        );

        // IO 卡顿测试
        findViewById(R.id.btn_io_block).setOnClickListener(v ->
                testIOBlock()
        );

        // 网络卡顿测试
        findViewById(R.id.btn_network_block).setOnClickListener(v ->
                testNetworkBlock()
        );

        // 内存分配卡顿测试
        findViewById(R.id.btn_memory_block).setOnClickListener(v ->
                testMemoryAllocationBlock()
        );

        // 数据库卡顿测试
        findViewById(R.id.btn_db_block).setOnClickListener(v ->
                testDatabaseBlock()
        );

        // JSON 解析卡顿测试
        findViewById(R.id.btn_json_block).setOnClickListener(v ->
                testJsonParseBlock()
        );

        // Bitmap 处理卡顿测试
        findViewById(R.id.btn_bitmap_block).setOnClickListener(v ->
                testBitmapBlock()
        );
    }

    // ==================== 测试方法 ====================

    /**
     * 测试 1：简单卡顿（1 秒）
     * 最基础的测试，直接 sleep 主线程
     */
    private void testSimpleBlock() {
        Log.d(TAG, "=============> 开始测试简单卡顿（1 秒）");

        try {
            // 直接 sleep 主线程 1 秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "=============> 简单卡顿测试完成");
        Toast.makeText(this, "1秒卡顿完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试 2：中等卡顿（2-3 秒）
     * 模拟数据处理导致的卡顿
     */
    private void testMediumBlock() {
        Log.d(TAG, "=============> 开始测试中等卡顿（2-3 秒）");

        long startTime = System.currentTimeMillis();

        // 模拟复杂计算
        long sum = 0;
        for (int i = 0; i < 100000000; i++) {
            sum += i;

            // 每 1000 万次循环做一次额外计算
            if (i % 10000000 == 0) {
                Math.sqrt(i);
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Log.d(TAG, "=============> 中等卡顿测试完成，耗时: " + duration + "ms");
        Toast.makeText(this, duration + "ms 卡顿完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试 3：严重卡顿（5+ 秒）
     * 模拟 ANR 级别的卡顿
     */
    private void testSevereBlock() {
        Log.d(TAG, "=============> 开始测试严重卡顿（5+ 秒）");

        try {
            // 直接 sleep 5 秒，模拟 ANR
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "=============> 严重卡顿测试完成");
        Toast.makeText(this, "5秒卡顿完成（ANR级别）", Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试 4：循环卡顿
     * 模拟死循环或无限循环
     */
    private void testLoopBlock() {
        Log.d(TAG, "=============> 开始测试循环卡顿（3 秒后自动停止）");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + 3000; // 3 秒后停止

        // 模拟死循环（实际会在 3 秒后退出）
        while (System.currentTimeMillis() < endTime) {
            // 空循环，消耗 CPU
            Math.random();
        }

        Log.d(TAG, "=============> 循环卡顿测试完成");
        Toast.makeText(this, "循环卡顿完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试 5：IO 卡顿
     * 模拟文件读写导致的卡顿
     */
    private void testIOBlock() {
        Log.d(TAG, "=============> 开始测试 IO 卡顿");

        try {
            File testFile = new File(getCacheDir(), "block_test.txt");
            FileOutputStream fos = new FileOutputStream(testFile);

            // 写入大量数据（10MB）
            byte[] buffer = new byte[1024]; // 1KB
            for (int i = 0; i < 10240; i++) { // 10240 * 1KB = 10MB
                fos.write(buffer);
                fos.flush(); // 强制刷新，增加卡顿
            }

            fos.close();

            // 读取数据
            FileInputStream fis = new FileInputStream(testFile);
            while (fis.read(buffer) != -1) {
                // 模拟处理数据
            }
            fis.close();

            // 删除测试文件
            testFile.delete();

            Log.d(TAG, "=============> IO 卡顿测试完成");
            Toast.makeText(this, "IO 卡顿测试完成", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试 6：网络卡顿
     * 模拟同步网络请求
     */
    private void testNetworkBlock() {
        Log.d(TAG, "=============> 开始测试网络卡顿");

        try {
            // 同步网络请求（不要在生产环境这样做！）
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Log.d(TAG, "=============> 网络请求成功，数据长度: " + response.length());
            }

            connection.disconnect();

            Log.d(TAG, "=============> 网络卡顿测试完成");
            Toast.makeText(this, "网络卡顿测试完成", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "网络请求失败: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 测试 7：内存分配卡顿
     * 模拟大量对象创建
     */
    private void testMemoryAllocationBlock() {
        Log.d(TAG, "=============> 开始测试内存分配卡顿");

        long startTime = System.currentTimeMillis();

        // 创建大量对象
        List<byte[]> memoryEater = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            // 每次分配 100KB
            byte[] data = new byte[100 * 1024];
            memoryEater.add(data);

            // 填充数据，增加处理时间
            for (int j = 0; j < data.length; j++) {
                data[j] = (byte) (j % 256);
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 清理内存
        memoryEater.clear();
        System.gc();

        Log.d(TAG, "=============> 内存分配卡顿测试完成，耗时: " + duration + "ms");
        Toast.makeText(this, "内存分配卡顿完成: " + duration + "ms",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试 8：数据库卡顿
     * 模拟大量数据库操作
     */
    private void testDatabaseBlock() {
        Log.d(TAG, "=============> 开始测试数据库卡顿");

        SQLiteDatabase db = null;
        try {
            // 创建临时数据库
            db = SQLiteDatabase.openOrCreateDatabase(
                    new File(getCacheDir(), "block_test.db"),
                    null
            );

            // 创建表
            db.execSQL("CREATE TABLE IF NOT EXISTS test_table " +
                    "(id INTEGER PRIMARY KEY, name TEXT, value INTEGER)");

            // 插入大量数据（不使用事务，故意卡顿）
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
                db.execSQL("INSERT INTO test_table (name, value) VALUES (?, ?)",
                        new Object[]{"Test_" + i, i});
            }

            // 查询数据
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM test_table WHERE value > 500",
                    null
            );

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            cursor.close();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Log.d(TAG, "=============> 数据库卡顿测试完成，插入 1000 条，查询到 " + count +
                    " 条，耗时: " + duration + "ms");
            Toast.makeText(this, "数据库卡顿完成: " + duration + "ms",
                    Toast.LENGTH_SHORT).show();

            // 清理
            db.execSQL("DROP TABLE test_table");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    /**
     * 测试 9：JSON 解析卡顿
     * 模拟大量 JSON 解析
     */
    private void testJsonParseBlock() {
        Log.d(TAG, "=============> 开始测试 JSON 解析卡顿");

        long startTime = System.currentTimeMillis();

        try {
            // 生成大型 JSON 字符串
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");

            for (int i = 0; i < 5000; i++) {
                if (i > 0) jsonBuilder.append(",");
                jsonBuilder.append("{")
                        .append("\"id\":").append(i).append(",")
                        .append("\"name\":\"User_").append(i).append("\",")
                        .append("\"age\":").append(20 + (i % 50)).append(",")
                        .append("\"email\":\"user").append(i).append("@test.com\"")
                        .append("}");
            }

            jsonBuilder.append("]");
            String jsonString = jsonBuilder.toString();

            // 解析 JSON
            JSONArray jsonArray = new JSONArray(jsonString);
            List<User> users = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                User user = new User();
                user.id = jsonObject.getInt("id");
                user.name = jsonObject.getString("name");
                user.age = jsonObject.getInt("age");
                user.email = jsonObject.getString("email");
                users.add(user);
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Log.d(TAG, "=============> JSON 解析卡顿测试完成，解析了 " + users.size() +
                    " 个对象，耗时: " + duration + "ms");
            Toast.makeText(this, "JSON 解析完成: " + duration + "ms",
                    Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试 10：Bitmap 处理卡顿
     * 模拟图片处理
     */
    private void testBitmapBlock() {
        Log.d(TAG, "=============> 开始测试 Bitmap 处理卡顿");

        long startTime = System.currentTimeMillis();

        // 创建大型 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);

        // 填充颜色（模拟图片处理）
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        for (int i = 0; i < 100; i++) {
            paint.setColor(Color.rgb(
                    (int) (Math.random() * 255),
                    (int) (Math.random() * 255),
                    (int) (Math.random() * 255)
            ));
            canvas.drawCircle(
                    (float) (Math.random() * 2000),
                    (float) (Math.random() * 2000),
                    50,
                    paint
            );
        }

        // 缩放 Bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);

        // 应用高斯模糊（耗时操作）
        Bitmap blurredBitmap = blurBitmap(scaledBitmap, 25);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 清理
        bitmap.recycle();
        scaledBitmap.recycle();
        blurredBitmap.recycle();

        Log.d(TAG, "=============> Bitmap 处理卡顿测试完成，耗时: " + duration + "ms");
        Toast.makeText(this, "Bitmap 处理完成: " + duration + "ms",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 简单的高斯模糊实现（故意不优化，用于测试卡顿）
     */
    private Bitmap blurBitmap(Bitmap bitmap, int radius) {
        Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0, g = 0, b = 0, count = 0;

                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int px = x + kx;
                        int py = y + ky;

                        if (px >= 0 && px < width && py >= 0 && py < height) {
                            int pixel = bitmap.getPixel(px, py);
                            r += Color.red(pixel);
                            g += Color.green(pixel);
                            b += Color.blue(pixel);
                            count++;
                        }
                    }
                }

                if (count > 0) {
                    output.setPixel(x, y, Color.rgb(r / count, g / count, b / count));
                }
            }
        }

        return output;
    }

    // 辅助类
    private static class User {
        int id;
        String name;
        int age;
        String email;
    }


}