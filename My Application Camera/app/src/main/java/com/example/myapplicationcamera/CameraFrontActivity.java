package com.example.myapplicationcamera;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFrontActivity extends AppCompatActivity {

    Camera mCamera;
    ImageButton btnCamera;
    CameraPreview cameraPreview;
    ImageButton btnImage;
    ImageButton btnChangeCamera;
    FrameLayout preview;
    private Camera.PictureCallback pictureCallback;
    private File mediaFile;
    private Boolean checkmediaFile = false;
    private int TYPE_CAMERA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_front);
        btnCamera = findViewById(R.id.btnCameraFront);
        btnImage = findViewById(R.id.btnImageFront);
        btnChangeCamera = findViewById(R.id.btnCameraChangeFront);
        mCamera = getCameraInstance();
        cameraPreview = new CameraPreview(this, mCamera);
        preview = findViewById(R.id.framelayout_camera_front);
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

//            Camera.Parameters parameters = c.getParameters();
//            List<String> focusModes = parameters.getSupportedFocusModes();
//            if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
//                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//                c.setParameters(parameters);
//            }

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
                    Intent intent = new Intent(CameraFrontActivity.this, PictureReviewActivity.class);
                    intent.putExtra("pathImage",pathImage);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CameraFrontActivity.this, "Không có ảnh nào", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void eventChangeCamera(){
        btnChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraFrontActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
