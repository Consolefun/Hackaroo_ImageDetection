package com.example.csroos.opencvtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    int TAKE_PHOTO_CODE = 1;
    ImageView objectImage;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private String[] permissions = {android.Manifest.permission.CAMERA};
    private boolean permission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        Button capture = (Button) findViewById(R.id.btn_take_photo);
        Button detectionimage = (Button) findViewById(R.id.btn_imagedetection);
        objectImage = (ImageView) findViewById(R.id.photo);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION);
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
//        }
        detectionimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageDetection.class);
                startActivity(intent);
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                callCamera(objectImage);

            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permission) finish();

    }

    public void callCamera(View v) {

        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }

    }

    //If the photo is captured then set the image view to the photo captured.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            objectImage.setImageBitmap(photo);
            Log.d("CameraDemo", "Pic saved");
        }
    }
}