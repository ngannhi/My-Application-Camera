package com.example.myapplicationcamera;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.hardware.Camera;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class MainActivity extends BaseAcivity {
    Camera mCamera;
    ImageButton btnCamera;
    CameraPreview cameraPreview;
    ImageButton btnImage;
    ImageButton btnChangeCamera;
    FrameLayout preview;
    private Camera.PictureCallback pictureCallback;
    private File mediaFile;
    private Boolean checkmediaFile = false;
    private int TYPE_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btnCamera);
        btnImage = findViewById(R.id.btnImage);
        btnChangeCamera = findViewById(R.id.btnCameraChange);

        if(getCameraInstance() == null){
            super.showAlertDialog("Không tìm thấy camera");
        }
        else{
            mCamera = getCameraInstance();
        }
        cameraPreview = new CameraPreview(this, mCamera, TYPE_CAMERA);
        preview = findViewById(R.id.framelayout_camera);
        preview.addView(cameraPreview);
        addEvents();
        eventImage();
        eventChangeCamera();
    }

    private void addEvents(){
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getOutPutMediaFile();
                if(pictureFile == null){
                    Log.d("MainActivity", "Error creating media file, check storage permissions");
                    return;
                }
                try {

                    FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                    camera.startPreview();
                    btnCamera.setEnabled(true);
                }catch (FileNotFoundException e){
                    Log.d("MainActivity", "File not found: " + e.getMessage());
                }catch (IOException e){
                    Log.d("MainActivity", "Error accessing file: " + e.getMessage());
                }
            }
        };
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);
                mCamera.takePicture(null, null, pictureCallback);
                btnCamera.setEnabled(false);
            }
        });


    }

    public Camera getCameraInstance(){
        Camera c = null;

        try {
            c = Camera.open(TYPE_CAMERA); // attempt to get a Camera instance

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private File getOutPutMediaFile(){
        //File mediaStorage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if(!mediaStorage.exists()){
            if(!mediaStorage.mkdirs()){
                Log.d("MainActivity", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaFile = new File(mediaStorage.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        checkmediaFile = true;
        return  mediaFile;
    }

    private void eventImage(){
            btnImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkmediaFile == true){
                        String pathImage = String.valueOf(mediaFile);
                        Intent intent = new Intent(MainActivity.this, PictureReviewActivity.class);
                        intent.putExtra("pathImage",pathImage);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Không tìm thấy ảnh", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    private void eventChangeCamera(){
        btnChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraFrontActivity.class);
                startActivity(intent);
                mCamera.release();
                finish();
            }
        });
    }



}
