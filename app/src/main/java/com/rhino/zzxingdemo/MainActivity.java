package com.rhino.zzxingdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rhino.zxing.CaptureActivity;
import com.rhino.zxing.util.CodeUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_SCAN = 2;

    private EditText etQrCode;
    private ImageView ivQrCode;

    private String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etQrCode = findViewById(R.id.et_qr_code_content);
        ivQrCode = findViewById(R.id.iv_qr_code);
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN && data != null) {
            String result = data.getStringExtra(CaptureActivity.KEY_RESULT);
            etQrCode.setText(result);
            Log.d(TAG, "result = " + result);
            Toast.makeText(this, "扫描成功：" + result, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickScan(View v) {
        startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE_SCAN);
    }

    public void onClickShare(View v) {
        String content = etQrCode.getText().toString();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap qrcodeBitmap = CodeUtils.createQRCode(
                TextUtils.isEmpty(content) ? "www.baidu.com" : content,
                dip2px(this, 300),
                icon);
        ivQrCode.setImageBitmap(qrcodeBitmap);
    }

    public static int dip2px(Context ctx, float dpValue) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }
}
