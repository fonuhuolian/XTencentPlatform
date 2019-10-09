package com.fonuhuolian.xtencentplatform;

import com.fonuhuolian.xtencentplatform.bean.WechatUserInfo;

public interface IWechaListener {

    void onFail(String errMsg);
    void onSuccess(WechatUserInfo loginResp);
    void onToken(String msg);
}
