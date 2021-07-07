package com.example.bustedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity {

    private Button mButtonSnap;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static Camera camera = null;
    private int cameraId = 0;
    private SurfaceView surfaceView;
    private SurfaceHolder previewHolder;
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback(){

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
            }   catch (Throwable t) {

            }
        }

        public void surfaceChanged(SurfaceHolder holder,int format, int width,int height) {
//            Camera.Parameters params = camera.getParameters();
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
//            Camera.Size size = getBestPreviewSize(width, height, params);
//            Camera.Size pictureSize=getSmallestPictureSize(params);
//            if (size != null && pictureSize != null) {
//                params.setPreviewSize(size.width, size.height);
//                params.setPictureSize(pictureSize.width,
//                        pictureSize.height);
//                camera.setParameters(params);
//                camera.startPreview();
//                inPreview=true;
//
//            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mButtonSnap = findViewById(R.id.buttonSnap);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        previewHolder = surfaceView.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                camera = Camera.open(cameraId);
            }
        }

        //monochrome effect
        Camera.Parameters parameters = camera.getParameters();
        parameters.setColorEffect(android.hardware.Camera.Parameters.EFFECT_MONO);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(previewHolder);
        }   catch (Throwable t) {

        }
        camera.startPreview();

        mButtonSnap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
            }
        });
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d("MainActivity", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
}