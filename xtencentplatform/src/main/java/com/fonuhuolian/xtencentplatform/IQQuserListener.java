package com.fonuhuolian.xtencentplatform;

public interface IQQuserListener {

    void onFail(String errMsg);
    void onSuccess(WechatLoginResp loginResp);
    void onToken(String msg);
}
