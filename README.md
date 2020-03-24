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
implementation 'com.github.fonuhuolian:XTencentPlatform:1.1.6.7'
```
`如遇jar包冲突(比如集成过微信支付) `
```
implementation ('com.github.fonuhuolian:XTencentPlatform:1.1.6.7'){
        exclude group: 'com.tencent.mm.opensdk'
}
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
- QQ登录
```

private QQLoginListener qqLoginListener;

qqLoginListener = new QQLoginListener() {

    @Override
    public void onLoginComplete(QQLoginResp resp) {
    
        // 如需用户信息，调用如下方法
        TencentLogin.onGetQQUserInfo(resp.getAccess_token(), resp.getOpenid(), true, new IQQUserListener() {
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
    public void onLoginError(UiError uiError) {

    }

    @Override
    public void onLoginCancel() {

    }
};

loginQq.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // 登录
        TencentLogin.onQQLogin(mActivity, qqLoginListener);
    }

});
```
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // 回调QQLoginListener方法
    TencentLogin.onQQActivityResult(requestCode, resultCode, data, qqLoginListener);
    super.onActivityResult(requestCode, resultCode, data);
}
```
- 微信登录

1.包名目录下创建`wxapi`文件夹

2.此包下创建名为`WXEntryActivity`的Activity,并实现`IWXAPIEventHandler`接口
```
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;
    private static final String APP_ID = "您应用的app_Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 由第三方App个性化展示登录、分享结果
        setContentView(R.layout.activity_wxpay_entry);

        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID,true);
        iwxapi.registerApp(APP_ID);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * TODO 微信登录、分享回调
     */
    @Override
    public void onResp(BaseResp baseResp) {

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 如果是微信登录回调获取用户信息
                if (TencentLogin.isWechatLoginCallBack(baseResp)) {      
                    TencentLogin.onGetWechatUserInfo(baseResp, new IWechatUserListener() {
                        @Override
                        public void onStart() {
                                   
                        }
       
                        @Override
                        public void onFail(String errMsg) {
                                   
                        }
       
                        @Override
                        public void onSuccess(WechatUserInfo info) {
                                   
                        }
                    });
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(mActivity, "取消", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(mActivity, "拒绝授权", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                break;
            default:
                break;
        }
    }
}
```
3.清单文件进行注册
```
<activity
    android:name=".wxapi.WXEntryActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"
    android:exported="true"
    android:launchMode="singleTask"
    android:screenOrientation="portrait" />
```
4.登录
```
loginWechat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // 登录
        TencentLogin.onWechatLogin(mActivity);
    }

});
```
- 媒体消息分享
```
TencentShare.onMediaMessageShare(context, titleStr, description, webUrl, imgUrl, type, listener)
```