package com.fonuhuolian.xtencentplatform.net;

import android.os.AsyncTask;
import android.util.Log;

import com.fonuhuolian.xtencentplatform.IQQUserListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class QQUserInfoAsync extends AsyncTask<String, Integer, String> {

    private IQQUserListener listener;

    public QQUserInfoAsync(IQQUserListener listener) {
        this.listener = listener;
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

        Log.e("ddd", result + "qq");

        try {

            // 转换成jsonObject
            JSONObject jsonObject = new JSONObject(result);

            // 获取成功的信息
            int ret = jsonObject.optInt("ret", 0);
            String msg = jsonObject.optString("msg", "");
            int is_lost = jsonObject.optInt("is_lost", 0);
            String nickname = jsonObject.optString("nickname", "");
            String gender = jsonObject.optString("gender", "男");
            String province = jsonObject.optString("province", "");
            String city = jsonObject.optString("city", "");
            String year = jsonObject.optString("year", "0");
            String constellation = jsonObject.optString("constellation", "");
            String figureurl = jsonObject.optString("figureurl", "");
            String figureurl_1 = jsonObject.optString("figureurl_1", "");
            String figureurl_2 = jsonObject.optString("figureurl_2", "");
            String figureurl_qq_1 = jsonObject.optString("figureurl_qq_1", "");
            String figureurl_qq_2 = jsonObject.optString("figureurl_qq_2", "");
            String figureurl_qq = jsonObject.optString("figureurl_qq", "");
            String figureurl_type = jsonObject.optString("figureurl_type", "0");
            String is_yellow_vip = jsonObject.optString("is_yellow_vip", "0");
            String vip = jsonObject.optString("vip", "0");
            String yellow_vip_level = jsonObject.optString("yellow_vip_level", "0");
            String level = jsonObject.optString("level", "0");
            String is_yellow_year_vip = jsonObject.optString("is_yellow_year_vip", "0");


//            errmsg = jsonObject.getString("errmsg");
//            int errcode = jsonObject.getInt("errcode");
//
//
//            if (TextUtils.isEmpty(errmsg)) {
//
//                List<String> privilegeList = new ArrayList<String>();
//
//
//                if (privilege != null && privilege.length() > 0) {
//
//                    for (int i = 0; i < privilege.length(); i++) {
//                        String s = (String) privilege.get(i);
//                        privilegeList.add(s);
//                    }
//                }
//
//                WechatUserInfo loginResp = new WechatUserInfo(openid, nickname, sex, province, city, country, headimgurl, unionid, privilegeList);
//
//                if (listener != null)
//                    listener.onSuccess(loginResp);
//
//            } else {
//
//                if (listener != null)
//                    listener.onFail(errmsg);
//
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
