package com.xlh.study.pluginskinsample.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

import androidx.core.content.ContextCompat;

/**
 * @author: Watler Xu
 * time:2020/1/13
 * description:皮肤资源管理类，用来管理皮肤插件apk的资源对象
 * version:0.0.1
 */
public class SkinManager {

    // 上下文
    private Context context;
    //皮肤插件的apk的资源对象
    private Resources resources;
    //外置皮肤插件apk的包名
    private String skinPackName;

    private static final SkinManager ourInstance = new SkinManager();

    public static SkinManager getInstance() {
        return ourInstance;
    }

    private SkinManager() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 根据皮肤apk的路径去获取到它的资源对象
     *
     * @param path
     */
    public void loadSkinApk(String path) {
        // 获取包管理器
        PackageManager packageManager = context.getPackageManager();
        // 获取皮肤插件apk的包信息类
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, packageManager.GET_ACTIVITIES);
        // 获取皮肤插件apk包名
        skinPackName = packageArchiveInfo.packageName;

        // Resources通过AssetManager去管理资源的，AssetManager才是直接跟资源打交道 而AssetManager不能直接new，只能通过反射获得
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            // 获取AssetManager对象中的addAssetPath方法,AssetManager通过addAssetPath来管理对应路径下的资源
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            // 执行这个addAssetPath方法
            addAssetPath.invoke(assetManager, path);
            // 获取到外置apk的resource对象,\当前显示度量,当前设备配置
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从外置插件apk中获取 资源对象中获取到颜色的资源
     *
     * @param id
     * @return
     */
    public int getColor(int id) {
        if (resources == null) {
            return id;
        }

        // 获取资源id的类型（@color /@drawable）
        String resourceTypeName = context.getResources().getResourceTypeName(id);
        // 获取资源id的属性名（@color后的名字）
        String resourceEntryName = context.getResources().getResourceEntryName(id);
        // 名字和类型匹配的资源对象中的id
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, skinPackName);
        if (identifier == 0) {
            return id;
        }
        return resources.getColor(identifier);
    }

    /**
     * 从外置插件apk中拿到drawable的资源id
     *
     * @param id
     * @return
     */
    public Drawable getDrawable(int id) {
        if (resources == null) {
            return ContextCompat.getDrawable(context, id);
        }

        // 获取资源id的类型（@color /@drawable）
        String resourceTypeName = context.getResources().getResourceTypeName(id);

        // 获取资源id的属性名（@color后的名字）
        String resourceEntryName = context.getResources().getResourceEntryName(id);
        // 就是colorAccent这个资源在外置apk中的id
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, skinPackName);

        if (identifier == 0) {
            return ContextCompat.getDrawable(context, id);
        }
        return resources.getDrawable(identifier);
    }
}
