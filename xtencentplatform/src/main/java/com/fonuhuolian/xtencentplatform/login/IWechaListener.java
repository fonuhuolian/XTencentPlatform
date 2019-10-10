package com.fonuhuolian.xtencentplatform.login;

import com.fonuhuolian.xtencentplatform.bean.WechatUserInfo;

public interface IWechaListener {

    // 获取信息开始前
    void onStart();

    // 获取失败
    void onFail(String errMsg);

    // 获取成功
    void onSuccess(WechatUserInfo info);
}
