package com.fonuhuolian.xtencentplatform;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.tencent.connect.common.Constants;
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

    // TODO QQ登录方法
    public static void onQQLogin(Activity activity, IUiListener listener) {
        Tencent mTencent = Tencent.createInstance(APP_ID_QQ, mContext);
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", listener);
        }
    }


    // TODO QQ登录在onActivityResult调用此方法
    public void onQQLoginResult(int requestCode, int resultCode, Intent data, IUiListener listener) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
    }


//    private void onShareImageAndText(QQType type,) {
//        final Bundle params = new Bundle();
//
//        if (type == QQType.QQ){
//            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        }else if (type == QQType.QZONE){
//            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
//        }
//
//
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
//        mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
//    }
}
