package com.liwenpeng.textimage.view.main.module.image.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jscheng.srich.note_edit.EditNoteActivity;
import com.liwenpeng.textimage.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GetImageUserActivity extends Activity {
    private Button button;
    private ImageView imageView_main;
    private FileInputStream is = null;
    public static Context activity;
    public static Uri uriImageview;
    private Bitmap bitmap_guiduhua;
    private Button button_xiangji;
    private Button button_xiangce;
    private Button button_xuanzhuan;
    private Button button_daoru;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_image_user);
        activity = this.getApplicationContext();
        //id绑定
        idBinding();
        //控件的点击事件的设置
        onClick();
    }

    private void idBinding() {
        button = (Button) findViewById(R.id.bitton);
        imageView_main = (ImageView)findViewById(R.id.main_imageview_tupian);

        button_xiangce = (Button) findViewById(R.id.xiangce);
        button_xiangji = (Button) findViewById(R.id.xiangji);
        button_xuanzhuan = (Button) findViewById(R.id.xuanzhuan);
        button_daoru = (Button) findViewById(R.id.daoru);
    }


    private void onClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap_guiduhua == null){
                    return;
                }
                bitmap_guiduhua = huiDuHua(bitmap_guiduhua);
                imageView_main.setImageBitmap(bitmap_guiduhua);
            }
        });
        button_daoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap_guiduhua == null){
                    return;
                }
                String path = saveBitmap(getBaseContext(), bitmap_guiduhua);
                Intent intent = new Intent(GetImageUserActivity.this, EditNoteActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                finish();
            }
        });
        button_xiangji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap_guiduhua == null){
                    return;
                }
//                bitmap_guiduhua = toRoundBitmap(bitmap_guiduhua);
                bitmap_guiduhua = scaleImage(bitmap_guiduhua,0.5f);
                imageView_main.setImageBitmap(bitmap_guiduhua);
            }
        });

        button_xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        button_xuanzhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap_guiduhua == null){
                    return;
                }
                bitmap_guiduhua = rotaingImageView(90, bitmap_guiduhua);
                imageView_main.setImageBitmap(bitmap_guiduhua);
            }
        });
    }


    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }
    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }


    /**
     * 旋转图片
     * @param angle 旋转角度
     * @param bitmap 要处理的Bitmap
     * @return 处理后的Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap)
    {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap){
        if (bitmap == null){
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height){
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else{
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return output;
    }

    public static  Bitmap  scaleImage(Bitmap origin, float ratio){
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode---->", ""+requestCode);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    /**
                     * 判断手机版本，因为在4.4版本都手机处理图片返回的方法就不一样了
                     * 4.4以后返回的不是真实的uti而是一个封装过后的uri 所以要对封装过后的uri进行解析
                     */

                    if (Build.VERSION.SDK_INT >=19){
                        //4.4系统一上用该方法解析返回图片
                        handleImageOnKitKat(data);
                    }else{
                        //4.4一下用该方法解析图片的获取
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case 2:
                Log.e("case2---->", "22222222222222222222");
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uriImageview,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImageview);
                    startActivityForResult(intent,3);//启动裁剪程序
                }
                break;
            case 3:
                if (resultCode == RESULT_OK){
                    try{
                        Log.e("case3---->", "3333333333333");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(uriImageview));
                        //将裁剪后的图片显示出来
                        bitmap_guiduhua = bitmap;
                        imageView_main.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
        }


    }
    /**
     * api 19以后
     *  4.4版本后 调用系统相机返回的不在是真实的uri 而是经过封装过后的uri，
     * 所以要对其记性数据解析，然后在调用displayImage方法尽心显示
     * @param data
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri 则通过id进行解析处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                //解析出数字格式id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                        "content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equals(uri.getScheme())){
            //如果不是document类型的uri，则使用普通的方式处理
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }

    /**
     * 4.4版本一下 直接获取uri进行图片处理
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    /**
     * 通过 uri seletion选择来获取图片的真实uri
     * @param uri
     * @param seletion
     * @return
     */
    private String getImagePath(Uri uri, String seletion){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,seletion,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 通过imagepath来绘制immageview图像
     * @param imagPath
     */
    private void displayImage(String imagPath){
        if (imagPath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagPath);
            imageView_main.setImageBitmap(bitmap);
            bitmap_guiduhua = bitmap;
        }else{
            Toast.makeText(this,"图片获取失败",Toast.LENGTH_SHORT).show();
        }
    }
    public  Bitmap huiDuHua(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        //Set the matrix to affect the saturation of colors.
        //A value of 0 maps the color to gray-scale.
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
