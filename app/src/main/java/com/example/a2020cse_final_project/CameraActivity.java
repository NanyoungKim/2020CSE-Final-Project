package com.example.a2020cse_final_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraActivity extends AppCompatActivity{
    private static CameraPreview surfaceView;
    private SurfaceHolder holder;
    private static Button camera_preview_btn;
    private static Camera mCamera;
    private int RESULT_PERMISSION = 100;
    public static CameraActivity getInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //카메라 프리뷰를 전체화면으로 보여주기 위해 셋팅
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestPermissionsCamera();
    }

    public static Camera getCamera(){
        return mCamera;
    }

    private void setInit(){
        getInstance=this;
        mCamera = Camera.open();

        setContentView(R.layout.activity_camera);

        surfaceView = (CameraPreview)findViewById(R.id.preview);

        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private boolean requestPermissionsCamera() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CameraActivity.this,new String[]{Manifest.permission.CAMERA},RESULT_PERMISSION);
            }
            else
                setInit();
        }
        else{
            setInit();
            return true;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(RESULT_PERMISSION == requestCode){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setInit();
            }
            else{

            }
            return;
        }
    }
}