package com.xlh.study.pluginskinsample.ui;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xlh.study.pluginskinsample.base.BaseActivity;
import com.xlh.study.pluginskinsample.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private final int REQUET_PERMISSION_CODE = 1;

    Button btnJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION_STORAGE, REQUET_PERMISSION_CODE);
            }
        }

        btnJump = findViewById(R.id.btn_jump);
        btnJump.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUET_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Toast.makeText(MainActivity.this, "申请权限为：" + permissions[i] + ",申请结果为：" + grantResults[i], Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_jump:
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

}
