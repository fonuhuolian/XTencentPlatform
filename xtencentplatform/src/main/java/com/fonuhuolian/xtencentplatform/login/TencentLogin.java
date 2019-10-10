package com.fonuhuolian.xtencentplatform.login;

import android.app.Activity;
import android.widget.Toast;

import com.fonuhuolian.xtencentplatform.TencentPlatformUtils;
import com.fonuhuolian.xtencentplatform.net.WechatTokenAsync;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class TencentLogin {

    // TODO 微信登录方法
    public static void onWechatLogin(Activity activity) {

        // 将该app注册到微信
        final IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, TencentPlatformUtils.getAppIdWechat());

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
    public static void onGetWechatUserInfo(Activity activity, String access_token, IWechaListener listener) {

        String userInfoUrl = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + TencentPlatformUtils.getAppIdWechat() + "&secret=" +
                TencentPlatformUtils.getAppSecretWechat() + "&code=" + access_token + "&grant_type=authorization_code";

        new WechatTokenAsync(listener).execute(userInfoUrl);
    }
}
