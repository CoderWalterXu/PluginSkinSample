# PluginSkinSample
插件化，加载外部皮肤apk换肤

### 原理
通过LayoutInflater的Factory2接口的onCreateView()监听xml解析过程，拿到符合换肤的view并存储到集合中     

通过反射调用AssetManager中的addAssetPath()加载外部皮肤apk中的资源，view集合遍历替换资源

![演示gif](github.com/CoderWalterXu/PluginSkinSample/blob/master/picture/skin.gif)
