package com.fonuhuolian.xtencentplatform.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;

/**
 * TODO 获取用户信息
 */
public class WechatShareAsync extends AsyncTask<String, Integer, Bitmap> {


    private WechatShareListener listener;

    public WechatShareAsync(WechatShareListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap thumbBmp = null;

        try {
            thumbBmp = BitmapFactory.decodeStream(new URL(params[0]).openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thumbBmp;
    }


    @Override
    protected void onPostExecute(Bitmap result) {
        if (listener != null)
            listener.thumbBmp(result);
    }
}
