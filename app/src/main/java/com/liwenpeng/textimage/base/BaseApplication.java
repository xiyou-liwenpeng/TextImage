package com.liwenpeng.textimage.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.jscheng.srich.route.Router;

public class BaseApplication extends Application {

    private static final String AK = "Di8PLjB0guzAjT9TNrWms7AE";
    private static final String SK = "pAUbLdTR72GDcSVg2sNoR9s0LGs6uVI3";

    private static Context mContext;

    public static Context getAppliction(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Router.init(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initBaiDuOcr();
            }
        });
        mContext = getApplicationContext();
    }

    private void initBaiDuOcr(){
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                Log.e("lwp","初始化成功 ");
                String token = result.getAccessToken();
            }
            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                Log.e("lwp","初始失败 "+error.getMessage());
            }
        }, getApplicationContext(), AK, SK);
    }

}
