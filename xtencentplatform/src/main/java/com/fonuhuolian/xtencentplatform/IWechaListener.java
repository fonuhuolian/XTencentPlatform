package com.fonuhuolian.xtencentplatform;

public interface IWechaListener {

    void onFail(String errMsg);
    void onSuccess(WechatLoginResp loginResp);
    void onToken(String msg);
}
