package com.example.instantmeasure;
//////////////////////////////////////
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class cam extends AppCompatActivity  {
    private Button btnCapture,btnsave;
    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);



        btnCapture = findViewById(R.id.btnTakePicture);
        btnsave = findViewById(R.id.save);
        imgCapture = findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                final Bitmap bmp = (Bitmap) data.getExtras().get("data");
                if(!Python.isStarted())
                Python.start(new AndroidPlatform(this));

                Python py = Python.getInstance();

                final PyObject pyo = py.getModule("hello");



                ///////////store image
               btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        // path to /data/data/yourapp/app_data/imageDir
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                        // Create imageDir
                        File file=new File(directory,"new.png");
                       // File file = new File(directory, "pre1" + ".jpg");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                 PyObject obj = pyo.callAttr("pyt");
                                imgCapture.setImageBitmap(BitmapFactory.decodeFile("/data/data/com.example.instantmeasure/app_imageDir/post.jpg"));
                                fos.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }










                    }

                });





            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}