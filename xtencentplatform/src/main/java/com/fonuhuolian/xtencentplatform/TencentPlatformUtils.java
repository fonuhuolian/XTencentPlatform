package com.fonuhuolian.xtencentplatform;

import android.app.Activity;
import android.app.Application;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

public class TencentPlatformUtils {

    private static String APP_ID_QQ;
    private static Application mContext;

    // 需要在Application进行全局初始化
    public static void init(Application context, String QQ_APP_ID) {
        mContext = context;
        APP_ID_QQ = QQ_APP_ID;
    }


    public static void login(Activity activity, String Scope, IUiListener listener) {
        Tencent mTencent = Tencent.createInstance(APP_ID_QQ, mContext);
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, Scope, listener);
        }
    }
}
