package com.liwenpeng.textimage.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;

public class PhoneImageUtil {

    public static void getPhoneImage() {

    }

    /**
     * 通过 uri seletion选择来获取图片的真实uri
     *
     * @param uri
     * @param seletion
     * @return
     */
    public static String getImagePath(Context context, Uri uri, String seletion) {
        String path = null;
        if (context == null || uri == null) {
            return "";
        }
        Cursor cursor = context.getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
    }

}
