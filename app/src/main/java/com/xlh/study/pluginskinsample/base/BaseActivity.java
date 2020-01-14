package com.xlh.study.pluginskinsample.base;

import android.app.Activity;
import android.os.Bundle;

import com.xlh.study.pluginskinsample.core.SkinFactory;
import com.xlh.study.pluginskinsample.core.SkinManager;

import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

/**
 * @author: Watler Xu
 * time:2020/1/13
 * description:
 * version:0.0.1
 */
public class BaseActivity extends Activity {

    private SkinFactory skinFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SkinManager.getInstance().setContext(this);

        skinFactory = new SkinFactory();

        // 监听xml的生成过程
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), skinFactory);
    }

    public void change() {
        skinFactory.apply();
    }

    /**
     * 让已经创建Activity的控件也换肤
     */
    @Override
    protected void onResume() {
        super.onResume();
        skinFactory.apply();
    }
}
