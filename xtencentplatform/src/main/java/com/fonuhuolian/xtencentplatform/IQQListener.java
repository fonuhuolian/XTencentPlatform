package com.fonuhuolian.xtencentplatform;

import com.tencent.tauth.IUiListener;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class IQQListener implements IUiListener {

    @Override
    public void onComplete(Object o) {

        String json = o.toString();

        QQLoginResp resp = new QQLoginResp();

        try {
            JSONObject jsonObject = new JSONObject(json);
            int ret = jsonObject.getInt("ret");
            String openid = jsonObject.getString("openid");
            String access_token = jsonObject.getString("access_token");
            String pay_token = jsonObject.getString("pay_token");
            int expires_in = jsonObject.getInt("expires_in");
            String pf = jsonObject.getString("pf");
            String pfkey = jsonObject.getString("pfkey");
            String msg = jsonObject.getString("msg");
            int login_cost = jsonObject.getInt("login_cost");
            int authority_cost = jsonObject.getInt("authority_cost");
            int query_authority_cost = jsonObject.getInt("query_authority_cost");
            long expires_time = jsonObject.getLong("expires_time");

            resp.setAccess_token(access_token);
            resp.setAuthority_cost(authority_cost);
            resp.setExpires_in(expires_in);
            resp.setExpires_time(expires_time);
            resp.setMsg(msg);
            resp.setOpenid(openid);
            resp.setLogin_cost(login_cost);
            resp.setPay_token(pay_token);
            resp.setPf(pf);
            resp.setPfkey(pfkey);
            resp.setQuery_authority_cost(query_authority_cost);
            resp.setRet(ret);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        onComplete(resp);

    }

    public abstract void onComplete(QQLoginResp resp);
}
