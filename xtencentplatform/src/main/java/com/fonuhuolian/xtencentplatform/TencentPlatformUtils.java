package com.fonuhuolian.xtencentplatform;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fonuhuolian.xtencentplatform.net.QQUserInfoAsync;
import com.fonuhuolian.xtencentplatform.net.WechatTokenAsync;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.Collections;

public class TencentPlatformUtils {

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


    // TODO 微信登录方法
    public static void onWechatLogin(Activity activity) {

        // 将该app注册到微信
        final IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, APP_ID_WECHAT);

        if (!wxapi.isWXAppInstalled()) {
            Toast.makeText(activity, "您尚未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        wxapi.sendReq(req);

    }


    // TODO 微信登录方法
    public static void onWechatLoginResult(Activity activity, String code, IWechaListener listener) {


        String tokenUrl = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID_WECHAT + "&secret=" +
                APP_SECRET_WECHAT + "&code=" + code + "&grant_type=authorization_code";

        new WechatTokenAsync(listener).execute(tokenUrl);
    }


    public static void getQQUserInfo(String ACCESS_TOKEN, String OPENID, IQQUserListener listener) {

        String url = "https://graph.qq.com/user/get_user_info?access_token=" + ACCESS_TOKEN + "&oauth_consumer_key=" + APP_ID_QQ + "&openid=" + OPENID;

        new QQUserInfoAsync(listener).execute(url);
    }


    // TODO QQ在onActivityResult调用此方法
    public static void onQQResult(int requestCode, int resultCode, Intent data, IQQListener listener) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
    }

    // TODO QQ登录方法
    public static void onQQLogin(Activity activity, IQQListener listener) {
        Tencent mTencent = Tencent.createInstance(APP_ID_QQ, mContext);
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", listener);
        }
    }


    // TODO 图文分享
    // QQ 分享 必填 链接、title 且不能为空
    // QQ空间 分享 必填 链接、title、至少一张图片 且不能为空
    public static void onQQShareImageAndText(Activity activity, IQQListener listener, QQType type, String title, String targetUrl, String summary, String... imageUrls) {

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
