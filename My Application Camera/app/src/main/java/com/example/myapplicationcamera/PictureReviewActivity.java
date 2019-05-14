package com.example.myapplicationcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PictureReviewActivity extends AppCompatActivity {
    //ImageButton btnBackCamera;
    ImageView imgReview;
    private String pathImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_review);
        //btnBackCamera = findViewById(R.id.btnBackCamera);
        imgReview = findViewById(R.id.image_view_review);
        showImage();
    }

    private void showImage(){
        Intent intent = getIntent();
        pathImage = intent.getStringExtra("pathImage");
        Log.d("PictureReviewActivity", "pathImage: " + pathImage);
        Bitmap bitmap = BitmapFactory.decodeFile(pathImage);
        imgReview.setImageBitmap(bitmap);


    }
}
