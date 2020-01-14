# PluginSkinSample
插件化，加载外部皮肤apk换肤

### 原理
通过LayoutInflater的Factory2接口的onCreateView()监听xml解析过程，拿到符合换肤的view     

通过反射调用AssetManager中的addAssetPath()加载外部资源
