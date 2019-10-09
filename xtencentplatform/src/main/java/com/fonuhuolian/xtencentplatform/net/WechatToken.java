package com.fonuhuolian.xtencentplatform.net;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.fonuhuolian.xtencentplatform.IWechaListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WechatToken extends AsyncTask<String, Integer, String> {

    private IWechaListener listener;
    private String APPID;

    public WechatToken(IWechaListener listener, String APPID) {
        this.listener = listener;
        this.APPID = APPID;
    }

    @Override
    protected String doInBackground(String... params) {

        String json = "";

        try {
            //1. URL
            URL url = new URL(params[0]);
            //2. HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置10秒的响应超时
            conn.setConnectTimeout(10000);
            //3. POST
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) { //获取状态码 200表示连接成功
                //获取输入流
                InputStream in = conn.getInputStream();
                //读取输入流
                byte[] b = new byte[1024 * 512]; //定义一个byte数组读取输入流
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); //定义缓存流来保存输入流的数据
                int len = 0;
                while ((len = in.read(b)) > -1) {  //每次读的len>-1 说明是是有数据的
                    baos.write(b, 0, len);  //三个参数  输入流byte数组   读取起始位置  读取终止位置
                }
                json = baos.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }


    @Override
    protected void onPostExecute(String result) {

        if (listener != null)
            listener.onToken(result);

        String errmsg = "";

        try {
            JSONObject jsonObject = new JSONObject(result);
            int expires_in = jsonObject.getInt("expires_in");
            String access_token = jsonObject.getString("access_token");
            String refresh_token = jsonObject.getString("refresh_token");
            String openid = jsonObject.getString("openid");
            String scope = jsonObject.getString("scope");
            String unionid = jsonObject.getString("unionid");
            int errcode = jsonObject.getInt("errcode");
            errmsg = jsonObject.getString("errmsg");

            if (!TextUtils.isEmpty(errmsg)) {
                String userInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
                new WechatUserInfo(listener).execute(userInfo);
            } else {
                if (listener != null)
                    listener.onFail(errmsg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
