package com.fonuhuolian.xtencentplatform;

import com.fonuhuolian.xtencentplatform.bean.QQUserInfo;

public interface IQQUserListener {

    void onFail(String errMsg);

    void onSuccess(QQUserInfo loginResp);

    void onToken(String msg);
}
