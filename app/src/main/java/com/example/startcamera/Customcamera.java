package com.example.startcamera;

import android.app.Activity;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;


import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;


public class Customcamera extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceView mPreview;
    private  SurfaceHolder mHolder;
    RelativeLayout relativeLayout;
    private  int cameraId=0;
    private  Camera.PictureCallback mpictureCallback=new Camera.PictureCallback(){

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File tempFile=new File("/sdcard/emp.png");
            try {
                FileOutputStream fos=new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                Intent intent=new Intent(Customcamera.this,ResultActivity.class);
                intent.putExtra("picpath",tempFile.getAbsolutePath());
                startActivity(intent);
                Customcamera.this.finish();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        mPreview= (SurfaceView) findViewById(R.id.preview);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_content);
        mHolder=mPreview.getHolder();
        mHolder.addCallback(this);
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
//                        if (success){mCamera.takePicture(null,null,mpictureCallback);
                            Log.d("ffh", "onAutoFocus: ");
                    }
                });
            }
        });
        mPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("relative",motionEvent.getX()+"  "+motionEvent.getY());
                return false;
            }
        });

//        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("relative",motionEvent.getX()+"  "+motionEvent.getY());
//
//                return false;
//            }
//        });
    }

    public void capture(View view){
        Log.d("", "onAutoFocus: ");
        Camera.Parameters parameters=mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800,400);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
//                if (success){mCamera.takePicture(null,null,mpictureCallback);
//                    Log.d("", "onAutoFocus: ");}
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null)
            mCamera=getCamera();
        if(mHolder!=null){setStartPreview(mCamera,mHolder);}
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private Camera getCamera(){
        Camera camera;
        try {
            camera=Camera.open(cameraId);
        } catch (Exception e) {
            camera=null;
            e.printStackTrace();
        }
        return camera;
    }


    private  void  setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera(){
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera=null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
