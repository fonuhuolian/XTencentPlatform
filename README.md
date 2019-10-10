# XTencentPlatform
TencentPlatform

> 添加依赖

`root build.gradle `
```
allprojects {
    repositories {
        ...
        maven {
            url 'https://jitpack.io'
        }
    }
}
```
`module build.gradle `
```
implementation 'com.github.fonuhuolian:XTencentPlatform:1.1.0'
```

> 混淆
```
-dontwarn org.fonuhuolian.xtencentplatform.**
-keep class org.fonuhuolian.xtencentplatform.**{*;}
-keep class * extends android.app.Dialog
```

> 用法

在Application#onCreate()中初始化`TencentPlatform`

```
TencentPlatform.init(this, QQ_APP_ID, WECHAT_APP_ID, WECHAT_APP_SECRET);
```
在`AndroidManifest`添加`AuthActivity`
```
<application>
     <activity
           android:name="com.tencent.tauth.AuthActivity"
           android:noHistory="true"
           android:launchMode="singleTask" >
          <intent-filter>
               <action android:name="android.intent.action.VIEW" />
               <category android:name="android.intent.category.DEFAULT" />
               <category android:name="android.intent.category.BROWSABLE" />
               <data android:scheme="tencent你的AppId" />
          </intent-filter>
     </activity>
<application>
```
QQ登录
```

private IUiListener iUiListener;

iUiListener = new IUiListener() {
    @Override
    public void onComplete(QQLoginResp resp) {
        TencentLogin.onGetQQUserInfo(resp.getAccess_token(), resp.getOpenid(), new IQQUserListener() {
            @Override
            public void onStart() {
                // 开启loading对话框
            }
        
        @Override
        public void onFail(String errMsg) {
            // 关闭loading对话框
            // 提示错误信息 errMsg
        }
        
        @Override
        public void onSuccess(QQUserInfo info) {
            // 关闭loading对话框
                                // 用户信息 info
                            }
                        });
    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
};

loginQq.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // 登录
        TencentLogin.onQQLogin(mActivity, iqqListener);
    }

});
```
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // 回调IUiListener方法
    TencentLogin.onQQActivityResult(requestCode, resultCode, data, iqqListener);
    super.onActivityResult(requestCode, resultCode, data);
}
```
