package com.fonuhuolian.xtencentplatform;

import com.google.gson.Gson;
import com.tencent.tauth.IUiListener;

public abstract class IQQListener implements IUiListener {

    @Override
    public void onComplete(Object o) {
        onComplete(new Gson().fromJson(o.toString(), QQLoginResp.class));
    }

    public abstract void onComplete(QQLoginResp resp);
}
