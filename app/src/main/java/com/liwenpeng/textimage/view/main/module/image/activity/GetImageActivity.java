package com.liwenpeng.textimage.view.main.module.image.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.jscheng.srich.utils.UriPathUtil;
import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.utils.PhoneImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class GetImageActivity extends Activity {

    private static IGetImageTxt mIGetImageTxt;
    private static boolean mIsWprd;

    public interface IGetImageTxt {
        void setImageTxt(String txt);
        void getImageInfor(String s);
    }

    public static void startGetImageActivity(Context context, IGetImageTxt iGetImageTxt, boolean isWord) {
        if (context == null) {
            return;
        }
        mIGetImageTxt = iGetImageTxt;
        mIsWprd = isWord;
        Intent intent = new Intent();
        intent.setClass(context, GetImageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneImageUtil.setStatusBarColor(this, R.color.transparent);
        startImage();
    }

    private void startImage() {
        if (ContextCompat.checkSelfPermission(GetImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(GetImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetImageActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode---->", "" + requestCode);
        finish();
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    /**
                     * 判断手机版本，因为在4.4版本都手机处理图片返回的方法就不一样了
                     * 4.4以后返回的不是真实的uti而是一个封装过后的uri 所以要对封装过后的uri进行解析
                     */
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4系统一上用该方法解析返回图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4一下用该方法解析图片的获取
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
        if (requestCode == 1) {
            Uri uri = data.getData();
            String path = UriPathUtil.getAbsulotePath(this, uri);
            if (mIGetImageTxt != null){
                mIGetImageTxt.getImageInfor(path);
            }
        }
    }


    /**
     * api 19以后
     * 4.4版本后 调用系统相机返回的不在是真实的uri 而是经过封装过后的uri，
     * 所以要对其记性数据解析，然后在调用displayImage方法尽心显示
     *
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = "";
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri 则通过id进行解析处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = PhoneImageUtil.getImagePath(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                        "content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = PhoneImageUtil.getImagePath(this, contentUri, null);
            }
        } else if ("content".equals(uri.getScheme())) {
            //如果不是document类型的uri，则使用普通的方式处理
            imagePath = PhoneImageUtil.getImagePath(this, uri, null);
        }
        Log.e("lwp", "图片的路径是" + imagePath);
        String finalImagePath = imagePath;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getImage(finalImagePath);
            }
        });
    }

    /**
     * 4.4版本一下 直接获取uri进行图片处理
     *
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = PhoneImageUtil.getImagePath(this, uri, null);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getImage(imagePath);
            }
        });
    }

    private void getImage(String filePath){
        // 通用文字识别参数设置
        if (!mIsWprd){
            return;
        }
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(this).recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                // 调用成功，返回GeneralResult对象
                JSONObject jsonObject = null;
                String words = "";
                Log.e("lwp","解析到的内容是 \n");
                try {
                    jsonObject = new JSONObject(result.getJsonRes());
                    JSONArray jsonArray = jsonObject.getJSONArray("words_result");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        words += "\n"+jsonObject1.getString("words");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mIGetImageTxt != null){
                    mIGetImageTxt.setImageTxt(words);
                }
            }
            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                Log.e("lwp","返回失败");
            }
        });
    }


}
