package com.example.startcamera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        String path=getIntent().getStringExtra("picpath");
        ImageView imageView= (ImageView) findViewById(R.id.pic);
        try {
            FileInputStream fis=new FileInputStream(path);
            Bitmap bitmap= BitmapFactory.decodeStream(fis);
            Matrix matrix=new Matrix();
            matrix.setRotate(270);
            bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
