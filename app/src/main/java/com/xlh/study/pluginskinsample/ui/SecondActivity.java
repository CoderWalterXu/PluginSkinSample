package com.xlh.study.pluginskinsample.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xlh.study.pluginskinsample.base.BaseActivity;
import com.xlh.study.pluginskinsample.uitl.FileUtil;
import com.xlh.study.pluginskinsample.R;
import com.xlh.study.pluginskinsample.core.SkinManager;

/**
 * @author: Watler Xu
 * time:2020/1/13
 * description:
 * version:0.0.1
 */
public class SecondActivity extends BaseActivity implements View.OnClickListener {

    Button btnLoad;
    Button btnChange;

    String apkPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnLoad = findViewById(R.id.btn_load);
        btnChange = findViewById(R.id.btn_change);

        btnLoad.setOnClickListener(this);
        btnChange.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load:
                apkPath = FileUtil.copyAssetAndWrite(SecondActivity.this, "plugin_skin-release-unsigned.apk");
                SkinManager.getInstance().loadSkinApk(apkPath);
                break;
            case R.id.btn_change:
                if (!TextUtils.isEmpty(apkPath)) {
                    change();
                } else {
                    Toast.makeText(getApplicationContext(), "请先下载", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
