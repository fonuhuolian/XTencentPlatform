package com.fonuhuolian.xtencentplatform.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.fonuhuolian.xtencentplatform.TencentPlatform;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class TencentShare {


    // TODO 媒体消息分享(用的最多的方式)
    // imgUrl QQ空间必须用网络图片
    public static void onMediaMessageShare(final Activity context, String titleStr, String description, String webUrl, final String imgUrl, final ShareType type, IQQShareListener listener) {

        String urlStartHttp = "http:" + File.separator + File.separator;
        String urlStartHttps = "https:" + File.separator + File.separator;

        if (TextUtils.isEmpty(titleStr)) {
            Toast.makeText(TencentPlatform.getmContext(), "分享失败(标题不能为空)", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (type) {

            case QQ:
                if (TextUtils.isEmpty(webUrl) || (!webUrl.startsWith(urlStartHttp) && !webUrl.startsWith(urlStartHttps))) {
                    Toast.makeText(TencentPlatform.getmContext(), "分享失败(网址格式不正确)", Toast.LENGTH_SHORT).show();
                    return;
                }

                Tencent mTencent = Tencent.createInstance(TencentPlatform.getAppIdQq(), context);

                final Bundle qqParams = new Bundle();
                qqParams.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                // 这条分享消息被好友点击后跳转URL（必填）
                qqParams.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
                // 分享的标题，最长30个字符（必填）
                qqParams.putString(QQShare.SHARE_TO_QQ_TITLE, titleStr);
                // 摘要 （选填）
                qqParams.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
                // 分享的图片 （选填）
                if (!TextUtils.isEmpty(imgUrl)) {
                    qqParams.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
                }

                mTencent.shareToQQ(context, qqParams, listener);
                break;


            case QZONE:
                if (TextUtils.isEmpty(webUrl) || (!webUrl.startsWith(urlStartHttp) && !webUrl.startsWith(urlStartHttps))) {
                    Toast.makeText(TencentPlatform.getmContext(), "分享失败(网址格式不正确)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(imgUrl) || (!imgUrl.startsWith(urlStartHttp) && !imgUrl.startsWith(urlStartHttps))) {
                    Toast.makeText(TencentPlatform.getmContext(), "分享失败(图片格式不正确)", Toast.LENGTH_SHORT).show();
                    return;
                }

                Tencent mTencents = Tencent.createInstance(TencentPlatform.getAppIdQq(), context);

                final Bundle qzoneParams = new Bundle();

                qzoneParams.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                // 分享的标题，最长30个字符（必填）
                qzoneParams.putString(QzoneShare.SHARE_TO_QQ_TITLE, titleStr);
                // 这条分享消息被好友点击后跳转URL（必填）
                qzoneParams.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl);
                // 摘要 （选填）
                qzoneParams.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);
                // 分享的图片
                ArrayList<String> arraylist = new ArrayList<>();
                Collections.addAll(arraylist, imgUrl);
                qzoneParams.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arraylist);

                mTencents.shareToQzone(context, qzoneParams, listener);
                break;


            case WECHAT_FRIEND:
            case WECHAT_CIRCLES:
            case WECHAT_COLLECTION:

                if (TextUtils.isEmpty(webUrl)) {
                    Toast.makeText(TencentPlatform.getmContext(), "分享失败(网址不能为空)", Toast.LENGTH_SHORT).show();
                    return;
                }

                //初始化一个WXWebpageObject，填写url
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = webUrl;

                //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
                final WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = titleStr;
                msg.description = description;

                File file = new File(TextUtils.isEmpty(imgUrl) ? "" : imgUrl);

                if (file.exists()) {

                    try {
                        Bitmap thumbBmp = BitmapFactory.decodeFile(imgUrl);
                        msg.thumbData = bitmap2Bytes(thumbBmp, 32);
                        thumbBmp.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;


                    req.scene = type.getScene();

                    //调用api接口，发送数据到微信
                    IWXAPI wxapi = WXAPIFactory.createWXAPI(context, TencentPlatform.getAppIdWechat(), true);
                    wxapi.registerApp(TencentPlatform.getAppIdWechat());
                    wxapi.sendReq(req);

                } else {

                    new WechatShareAsync(new WechatShareListener() {
                        @Override
                        public void thumbBmp(Bitmap bitmap) {

                            try {
                                msg.thumbData = bitmap2Bytes(bitmap, 32);
                                bitmap.recycle();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //构造一个Req
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg;


                            req.scene = type.getScene();

                            //调用api接口，发送数据到微信
                            IWXAPI wxapi = WXAPIFactory.createWXAPI(context, TencentPlatform.getAppIdWechat(), true);
                            wxapi.registerApp(TencentPlatform.getAppIdWechat());
                            wxapi.sendReq(req);

                        }
                    }).execute(imgUrl);
                }
                break;
        }

    }


    // TODO 分享视频
    public static void onVideoShare(final Activity context, String titleStr, String description, String videoUrl, final String imgUrl, final ShareType type, IQQShareListener listener) {

        String urlStartHttp = "http:" + File.separator + File.separator;
        String urlStartHttps = "https:" + File.separator + File.separator;

//        if (TextUtils.isEmpty(titleStr)) {
//            Toast.makeText(TencentPlatform.getmContext(), "分享失败(标题不能为空)", Toast.LENGTH_SHORT).show();
//            return;
//        }

        switch (type) {

            case QQ:
                break;
            case QZONE:
                break;


            case WECHAT_FRIEND:
            case WECHAT_CIRCLES:
            case WECHAT_COLLECTION:

                if (TextUtils.isEmpty(videoUrl)) {
                    Toast.makeText(TencentPlatform.getmContext(), "分享失败(视频地址不能为空)", Toast.LENGTH_SHORT).show();
                    return;
                }


                //初始化一个WXVideoObject，填写url
                WXVideoObject video = new WXVideoObject();
                // 不能为空 掉不起来微信
                video.videoUrl = videoUrl;

                //用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
                final WXMediaMessage msg = new WXMediaMessage(video);
                msg.title = titleStr;
                msg.description = description;

                File file = new File(TextUtils.isEmpty(imgUrl) ? "" : imgUrl);

                if (file.exists()) {

                    try {
                        Bitmap thumbBmp = BitmapFactory.decodeFile(imgUrl);
                        msg.thumbData = bitmap2Bytes(thumbBmp, 32);
                        thumbBmp.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("video");
                    req.message = msg;
                    req.scene = type.getScene();

                    //调用api接口，发送数据到微信
                    IWXAPI wxapi = WXAPIFactory.createWXAPI(context, TencentPlatform.getAppIdWechat(), true);
                    wxapi.registerApp(TencentPlatform.getAppIdWechat());
                    wxapi.sendReq(req);

                } else {

                    new WechatShareAsync(new WechatShareListener() {
                        @Override
                        public void thumbBmp(Bitmap bitmap) {

                            try {
                                msg.thumbData = bitmap2Bytes(bitmap, 32);
                                bitmap.recycle();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //构造一个Req
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("video");
                            req.message = msg;
                            req.scene = type.getScene();

                            //调用api接口，发送数据到微信
                            IWXAPI wxapi = WXAPIFactory.createWXAPI(context, TencentPlatform.getAppIdWechat(), true);
                            wxapi.registerApp(TencentPlatform.getAppIdWechat());
                            wxapi.sendReq(req);

                        }
                    }).execute(imgUrl);
                }
                break;
        }

    }


    /**
     * 构建一个唯一标志
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();

    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     */
    private static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }

}
