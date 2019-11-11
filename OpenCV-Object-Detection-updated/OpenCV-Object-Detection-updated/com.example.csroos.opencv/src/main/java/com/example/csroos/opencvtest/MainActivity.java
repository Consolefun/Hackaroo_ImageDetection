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

import org.opencv.photo.Photo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends AppCompatActivity {

    static final int TAKE_PHOTO_CODE = 1;
    private String pictureFilePath;
    ImageView objectImage;
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
        findViewById(R.id.btn_save_local).setOnClickListener(saveLocal);

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
    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "test" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }
    private  View.OnClickListener saveLocal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addToLocal();
        }
    };
    private void addToLocal(){
        Intent localintent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(file);
        localintent.setData(picUri);
        this.sendBroadcast(localintent);
    }

    public void callCamera(View v) {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);


            File pictureFile = null;
            try{
                pictureFile = getPictureFile();
            }catch (IOException e){
                Toast.makeText(this, "Photo file can't be created", Toast.LENGTH_SHORT).show();
                return;
            }
            if(pictureFile != null){
                Uri photo = FileProvider.getUriForFile(this, "com.example.csroos.fileprovider", pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        }

    }


    //If the photo is captured then set the image view to the photo captured.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK){
            File imgFile = new File(pictureFilePath);
            if(imgFile.exists()){
                objectImage.setImageURI(Uri.fromFile(imgFile));
            }
        }

    }


}