package com.fonuhuolian.xtencentplatform.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.fonuhuolian.xtencentplatform.TencentPlatform;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class TencentShare {


    // TODO 微信分享
    // TODO titleStr 不能为null且不能为空字符串
    // TODO webUrl 不能为null且不能为空字符串
    public static void onShare(final Context context, String titleStr, String description, String webUrl, final String imgUrl, final ShareType type) {

        switch (type) {

            case QQ:

                break;


            case QZONE:
                break;


            case WECHAT_FRIEND:
            case WECHAT_CIRCLES:
            case WECHAT_COLLECTION:

                if (TextUtils.isEmpty(titleStr) || TextUtils.isEmpty(webUrl)) {
                    Toast.makeText(TencentPlatform.getmContext(), "分享失败(标题、网址不能为空)", Toast.LENGTH_SHORT).show();
                    return;
                }

                //初始化一个WXWebpageObject，填写url
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = webUrl;

                //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
                final WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = titleStr;
                msg.description = description;

                File file = new File(imgUrl);

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
                    IWXAPI wxapi = WXAPIFactory.createWXAPI(context, TencentPlatform.getAppIdWechat());
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
                            IWXAPI wxapi = WXAPIFactory.createWXAPI(context, TencentPlatform.getAppIdWechat());
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
    protected static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();

    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     */
    protected static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
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
