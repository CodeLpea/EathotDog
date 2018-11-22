package com.example.administrator.eathotdog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;
import java.io.FileInputStream;

import static android.content.ContentValues.TAG;

public class EatHotDog extends AppCompatActivity implements View.OnClickListener {
    private static  String  TAG="EatHotDog";
    private Button btnOn;
    private ImageView imgeOn;
    private Bitmap getLocalImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_hot_dog);
        if (hasPermission()) {//判断是否有权限
            init();//初始化控件
        } else {
            requestPermission();
        }
    }

    /**
     * 判断是否有权限
     * @return
     */
    private boolean hasPermission() {
        Log.i(TAG, "hasPermission: 判断是否有权限");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /**
     * 请求权限
     */

    private void requestPermission() {
        Log.i(TAG, "requestPermission: 请求权限");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "requestPermission: ");
            }
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    private void init() {
        btnOn=(Button)findViewById(R.id.bt_on);
        imgeOn=(ImageView)findViewById(R.id.iv_on);
        //监听btn
        btnOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      switch(view.getId()){
          case R.id.bt_on:
              //执行显示图片的操作
              doShow();
              break;
              default:
                  Log.i(TAG, "onClick: ");
        }

    }

    /**
     * 开始表演
     */

    private void doShow() {
        Log.i(TAG, "doShow: ");
        //读取
        getLocalImage=readBitmapFromFileDescriptor(Environment.getExternalStorageDirectory().getPath()+ File.separator+"eathotdog.jpg",300,300);
        //显示
        imgeOn.setImageBitmap(getLocalImage);
    }

    /**
     * 读取本地文件的方法
     */
    public static Bitmap readBitmapFromFileDescriptor(String filePath, int width, int height) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;

            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
            Log.e(TAG, "readBitmapFromFileDescriptor: "+ex.getMessage() );
        }
        return null;
    }
}
