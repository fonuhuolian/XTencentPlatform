package com.fonuhuolian.xtencentplatform;

import android.app.Application;

public class TencentPlatform {

    private static String APP_ID_QQ;
    private static String APP_ID_WECHAT;
    private static String APP_SECRET_WECHAT;
    private static Application mContext;

    // 需要在Application进行全局初始化
    public static void init(Application context, String QQ_APP_ID, String WECHAT_APP_ID, String WECHAT_APP_SECRET) {
        mContext = context;
        APP_ID_QQ = QQ_APP_ID;
        APP_ID_WECHAT = WECHAT_APP_ID;
        APP_SECRET_WECHAT = WECHAT_APP_SECRET;
    }


    public static String getAppIdQq() {
        return APP_ID_QQ;
    }

    public static String getAppIdWechat() {
        return APP_ID_WECHAT;
    }

    public static String getAppSecretWechat() {
        return APP_SECRET_WECHAT;
    }

    public static Application getmContext() {
        return mContext;
    }
}
