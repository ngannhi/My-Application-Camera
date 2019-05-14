package com.example.myapplicationcamera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private  Context context;
    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            setFocus(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters pare = camera.getParameters();
        List<String> focusModes = pare.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            pare.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        List<Camera.Size> sizes = pare.getSupportedPictureSizes();
        Camera.Size mSize = sizes.get(2);

//        for (Camera.Size size : sizes){
//            mSize = size ;
//        }

        if(this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE)
        {
            pare.set("orientation", "portrait");
            //camera.startPreview();
            camera.setDisplayOrientation(90);
            pare.setRotation(90);
        }else
        {
            pare.set("orientation", "portrait");
            //camera.startPreview();
            camera.setDisplayOrientation(0);
            pare.setRotation(0);
        }
        pare.setPictureSize(mSize.width,mSize.height);
        camera.setParameters(pare);
        try{
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }

        catch (Exception ex){
            Log.d("CameraPrview","CameraPreview Error:" + ex.getMessage());
        }
//        try {
//            camera.setPreviewDisplay(holder);
//            //camera.setDisplayOrientation(90);
//            setCameraDisplayOrientation((Activity) context, 0, camera);
//            camera.startPreview();
//
//        }catch (Exception ex){
//            Log.d("CameraPrview","CameraPreview Error:" + ex.getMessage());
//        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("CameraPreview", "width: " + width + "height: " + height);
        if(surfaceHolder.getSurface() == null){
            return;
        }
        try{
            camera.stopPreview();
        }catch (Exception ex){

        }

        try {
//            Camera.Parameters parameters=camera.getParameters();
//            parameters.setPreviewSize(3000,4000);
            camera.setPreviewDisplay(holder);
            //setCameraDisplayOrientation((Activity) context, 0, camera);
            camera.startPreview();
        }catch (Exception ex){

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void setFocus(SurfaceHolder holder) throws IOException {
        Camera.Parameters params = camera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        camera.setParameters(params);
        camera.setPreviewDisplay(holder);
        camera.startPreview();
    }
/*
    public void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees ) % 360;
            result = (360 - result) % 360;
        }else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }*/
}
