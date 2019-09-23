package com.fonuhuolian.xtencentplatform;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.Collections;

public class TencentPlatformUtils {

    private static String APP_ID_QQ;
    private static Application mContext;

    // 需要在Application进行全局初始化
    public static void init(Application context, String QQ_APP_ID) {
        mContext = context;
        APP_ID_QQ = QQ_APP_ID;
    }

    // TODO QQ登录方法
    public static void onQQLogin(Activity activity, IQQListener listener) {
        Tencent mTencent = Tencent.createInstance(APP_ID_QQ, mContext);
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", listener);
        }
    }


    // TODO QQ登录在onActivityResult调用此方法
    public static void onQQLoginResult(int requestCode, int resultCode, Intent data, IQQListener listener) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
    }


    // TODO 图文分享
    public static void onShareImageAndText(Activity activity, IUiListener listener, QQType type, String title, String targetUrl, String summary, String... imageUrls) {

        Tencent mTencent = Tencent.createInstance(APP_ID_QQ, mContext);

        final Bundle params = new Bundle();

        if (type == QQType.QQ) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            // 这条分享消息被好友点击后跳转URL（必填）
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            // 分享的标题，最长30个字符（必填）
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);

            // 摘要 （选填）
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
            // 分享的图片 （选填）
            if (imageUrls != null && imageUrls.length > 0) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls[0]);
            }

            mTencent.shareToQQ(activity, params, listener);

        } else if (type == QQType.QZONE) {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            // 分享的标题，最长30个字符（必填）
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
            // 这条分享消息被好友点击后跳转URL（必填）
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);

            // 摘要 （选填）
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
            // 分享的图片 （选填）
            if (imageUrls != null && imageUrls.length > 0) {
                ArrayList<String> arraylist = new ArrayList<>();
                Collections.addAll(arraylist, imageUrls);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arraylist);
            }

            mTencent.shareToQzone(activity, params, listener);
        }
    }
}
