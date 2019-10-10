package com.fonuhuolian.xtencentplatform.login;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.fonuhuolian.xtencentplatform.IQQListener;
import com.fonuhuolian.xtencentplatform.TencentPlatform;
import com.fonuhuolian.xtencentplatform.net.QQUserInfoAsync;
import com.fonuhuolian.xtencentplatform.net.WechatTokenAsync;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

public class TencentLogin {

    // TODO 微信登录方法
    public static void onWechatLogin(Activity activity) {

        // 将该app注册到微信
        final IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, TencentPlatform.getAppIdWechat());

        if (!wxapi.isWXAppInstalled()) {
            Toast.makeText(activity, "您尚未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        wxapi.sendReq(req);

    }


    // TODO 微信获取用户信息（需要在onWechatLogin()回调里取）
    public static void onGetWechatUserInfo(String access_token, IWechatUserListener listener) {

        String tokenUrl = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + TencentPlatform.getAppIdWechat() + "&secret=" +
                TencentPlatform.getAppSecretWechat() + "&code=" + access_token + "&grant_type=authorization_code";

        new WechatTokenAsync(listener).execute(tokenUrl);
    }

    // TODO QQ登录方法
    public static void onQQLogin(Activity activity, IQQListener listener) {
        Tencent mTencent = Tencent.createInstance(TencentPlatform.getAppIdQq(), activity);
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", listener);
        }
    }

    // TODO QQ在onActivityResult调用此方法
    public static void onQQActivityResult(int requestCode, int resultCode, Intent data, IQQListener listener) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
    }

    // TODO  QQ获取用户信息（需要在IQQListener里通过QQLoginResp取OPENID）
    public static void onGetQQUserInfo(String ACCESS_TOKEN, String OPENID, IQQUserListener listener) {

        String infoUrl = "https://graph.qq.com/user/get_user_info?access_token=" + ACCESS_TOKEN + "&oauth_consumer_key=" + TencentPlatform.getAppIdQq() + "&openid=" + OPENID;

        new QQUserInfoAsync(listener).execute(infoUrl);
    }
}
