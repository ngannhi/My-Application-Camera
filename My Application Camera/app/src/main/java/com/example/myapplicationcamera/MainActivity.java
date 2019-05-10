package com.example.myapplicationcamera;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.hardware.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {
    Camera mCamera;
    ImageButton btnCamera;
    CameraPreview cameraPreview;
    private Camera.PictureCallback pictureCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btnCamera);

        mCamera = getCameraInstance();
        cameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.framelayout_camera);
        preview.addView(cameraPreview);
        addEvents();
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
            }
        });
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private static File getOutPutMediaFile(){
        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if(!mediaStorage.exists()){
            if(!mediaStorage.mkdirs()){
                Log.d("MainActivity", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorage.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return  mediaFile;
    }
}
