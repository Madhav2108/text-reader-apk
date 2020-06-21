package com.example.textdetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {
    Button click,detect;
    ImageView image;
    Bitmap image_rec;
    private int CAMERA_REQUEST=100;
    FirebaseVisionTextRecognizer detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=findViewById(R.id.image);
        click=findViewById(R.id.click);
        detect=findViewById(R.id.detect);
        detector= FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();


        click.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAMERA_REQUEST);
            }
        });
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectText(image_rec);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CAMERA_REQUEST){
            Bundle extra=data.getExtras();
            image_rec=(Bitmap) extra.get("data");

            image.setImageBitmap(image_rec);
        }
    }





    public void detectText(Bitmap b){
        FirebaseVisionImage f_image=FirebaseVisionImage.fromBitmap(b);
        detector.processImage(f_image).addOnCompleteListener(new OnCompleteListener<FirebaseVisionText>() {
            @Override
            public void onComplete(@NonNull Task<FirebaseVisionText> task) {

                List<FirebaseVisionText.TextBlock> blocks= task.getResult().getTextBlocks();
                    String d="";
                for(int i=0;i<blocks.size();i++){
                    List<FirebaseVisionText.Line> lines=blocks.get(i).getLines();

                    for(int j=0;j<lines.size();j++){

                        String k=lines.get(j).toString();
                        d+=k+"\n";

                    }
                        d+="\n";
                }
                Toast.makeText(MainActivity.this,""+d,Toast.LENGTH_LONG).show();
            }
        });




    }
}
