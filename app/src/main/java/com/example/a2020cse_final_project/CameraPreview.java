package com.example.a2020cse_final_project;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    public List<Camera.Size> listPreviewSize;
    private Camera.Size previewSize;
    private Context context;

    public CameraPreview(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        mCamera = CameraActivity.getCamera();
        if(mCamera==null)
            mCamera=Camera.open();
        listPreviewSize = mCamera.getParameters().getSupportedPreviewSizes();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            if(mCamera==null)
                mCamera=Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();

            //카메라의 회전이 가로/세로 일때 화면을 설정
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                parameters.set("orientation","portrait");
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }
            else{
                parameters.set("orientation","landscape");
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();;
            mCamera.autoFocus(new Camera.AutoFocusCallback(){

                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if(success){

                    }
                }
            });
        }catch(IOException e){
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(surfaceHolder.getSurface()==null)
            return;
        try{
            mCamera.stopPreview();
            Camera.Parameters parameters = mCamera.getParameters();

            int rotation=CameraActivity.getInstance.getWindowManager().getDefaultDisplay().getRotation();
            if(rotation== Surface.ROTATION_0){
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }else if(rotation ==Surface.ROTATION_90){
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            else if(rotation ==Surface.ROTATION_180){
                mCamera.setDisplayOrientation(270);
                parameters.setRotation(270);
            }
            else {
                mCamera.setDisplayOrientation(180);
                parameters.setRotation(180);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        setMeasuredDimension(width,height);

        if(listPreviewSize!=null)
            previewSize = getPreviewSize(listPreviewSize,width,height);
    }

    public Camera.Size getPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double)h/w;

        if(sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for(Camera.Size size : sizes){
            double ratio =(double)size.width/size.height;
            if(Math.abs(ratio-targetRatio)>ASPECT_TOLERANCE)
                continue;
            if(Math.abs(size.height-targetHeight)<minDiff){
                optimalSize = size;
                minDiff = Math.abs(size.height-targetHeight);
            }
        }

        if(optimalSize==null){
            minDiff= Double.MAX_VALUE;
            for(Camera.Size size : sizes){
                if(Math.abs(size.height-targetHeight)<minDiff){
                    optimalSize=size;
                    minDiff= Math.abs(size.height-targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
