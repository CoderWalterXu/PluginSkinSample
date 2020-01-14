package com.xlh.study.pluginskinsample.core;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Watler Xu
 * time:2020/1/13
 * description:
 * version:0.0.1
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    // 存储符合换肤的控件，包括已经创建了的activity中的控件
    private List<SkinView> viewList = new ArrayList<>();
    // 系统控件
    private static final String[] prsfixList = {
            "android.widget.", "android.view.", "android.webkit."
    };

    /**
     * @param parent
     * @param name    控件名，比如TextView
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        // 监听xml的生成过程，控制这些控件创建的过程
        View view = null;
        // 判断这个view是否是自定义的view，还是系统的view
        if (name.contains(".")) {
            // 实例化view，调用的onCreateView是下面的
            view = onCreateView(name, context, attrs);
        } else {
            for (String s : prsfixList) {
                view = onCreateView(s + name, context, attrs);
                if (view != null) {
                    break;
                }
            }
        }

        if (view != null) {
            //如果控件不为空，判断该是否需要换肤，也就是是否含有background、src、color、textColor等属性
            parseView(view, name, attrs);
        }

        return view;
    }

    /**
     * 判断该控件是否需要换肤，如果需要就存储到集合中
     */
    private void parseView(View view, String name, AttributeSet attrs) {

        // 存储需要换肤控件的集合
        List<SkinItem> itemList = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            // 获取属性名
            String attributeName = attrs.getAttributeName(i);

            // 获取属性的资源id
            String attributeValueId = attrs.getAttributeValue(i);

            // 判断该控件是不是需要换肤的控件，也就是是否含有background、src、color、textColor等属性
            if (attributeName.contains("background") || attributeName.contains("textColor") || attributeName.contains("src") || attributeName.contains("color")) {
                // 获取资源id
                int id = Integer.parseInt(attributeValueId.substring(1));
                // 获取属性值的类型
                String resourceTypeName = view.getResources().getResourceTypeName(id);
                // 获取属性值的名字
                String resourceEntryName = view.getResources().getResourceEntryName(id);

                SkinItem skinItem = new SkinItem(attributeName, id, resourceEntryName, resourceTypeName);
                // 添加到集合
                itemList.add(skinItem);
            }
        }

        // 如果长度大于0，就表示当前控件需要换肤
        if (itemList.size() > 0) {
            SkinView skinView = new SkinView(view, itemList);
            viewList.add(skinView);
            skinView.apply();
        }
    }

    /**
     * 给所有存储起来的view换肤
     */
    public void apply() {
        for (SkinView skinView : viewList) {
            skinView.apply();
        }
    }

    /**
     * 控件实例化
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            // 获取该name对应的class类对象
            Class aClass = context.getClassLoader().loadClass(name);

            Constructor<? extends View> constructor = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});

            view = constructor.newInstance(context, attrs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 封装每一条的属性的对象
     */
    class SkinItem {
        // 属性名 background
        String name;

        // 属性值的资源id
        int resId;

        // 属性值的名字，如colorPrimary
        String entryName;

        // 属性值的类型，如color、mipmap
        String typeName;

        public SkinItem(String name, int resId, String entryName, String typeName) {
            this.name = name;
            this.resId = resId;
            this.entryName = entryName;
            this.typeName = typeName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getEntryName() {
            return entryName;
        }

        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    /**
     * 把单个控件view封装
     */
    class SkinView {
        //
        private View view;

        // 控件的所有属性的集合
        List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        /**
         * 给单个控件换肤
         */
        public void apply() {
            for (SkinItem skinItem : list) {
                if (skinItem.getName().equals("background")) {
                    // 从皮肤插件apk资源对象中获取相匹配的id，进行设置
                    if (skinItem.getTypeName().equals("color")) {
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        } else {
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                } else if (skinItem.getName().equals("src")) {
                    if (skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")) {
                        if (view instanceof ImageView) {
                            ((ImageView) view).setImageDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                } else if (skinItem.getName().equals("textColor")) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (view instanceof Button) {
                        ((Button) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }

                }
            }
        }
    }
}
