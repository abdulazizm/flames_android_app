package com.fpe.flames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Result extends AppCompatActivity {

    Button refer,share;
    TextView output;

    ImageView animate;
    String op,result;
    InterstitialAd mInterstitialAd;
    KonfettiView viewKonfetti;
    ObjectAnimator oa1;
//    float a=1F,b=0F,temp;
//
//    int times = 0;
//    int[] arr = {R.drawable.friends, R.drawable.love, R.drawable.affection, R.drawable.marriage, R.drawable.sibling};
    @Override
    protected void onCreate(Bundle savedInstanceState) { ;
        super.onCreate(savedInstanceState);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9620947912803669/8106268655");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        setContentView(R.layout.activity_result);
        Intent intent=this.getIntent();
        result=intent.getStringExtra("result");
        op =intent.getStringExtra("output");

        refer= findViewById(R.id.refer);
        output = findViewById(R.id.output);
        share = findViewById(R.id.share);
        animate = findViewById(R.id.animate);
        viewKonfetti= findViewById(R.id.viewKonfetti);

        animate.setImageResource(R.drawable.loading);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoragePermissionGranted()) {
                    store(getScreenShot(), "FLAMES_SCREENSHOT.png");
                    shareImage("FLAMES_SCREENSHOT.png");
                }
            }
        });

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoragePermissionGranted()) {
                    store(BitmapFactory.decodeResource(getResources(), R.mipmap.flames_icon_foreground), "FLAMES_REFER.png");
                    shareImage("FLAMES_REFER.png");
                }
            }
        });

        output.setText(op);
        flipImage(result);


        //Set size of the popup activity
        DisplayMetrics d=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        int w=d.widthPixels;
        int h=d.heightPixels;
        getWindow().setLayout((int)(w*0.9),(int)(h*0.6));
    }

    void flipImage(final String output_str) {
        oa1 = ObjectAnimator.ofFloat(animate, "scaleX", 1f, 0f,0f,1f,0f,1f,0f,1f,0f,1f,0f,1f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(animate, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                switch(output_str.charAt(0))
                {
                    case 'F':
                        animate.setImageResource(R.drawable.friends);
                        break;
                    case 'L':
                        animate.setImageResource(R.drawable.love);
                        break;
                    case 'A':
                        animate.setImageResource(R.drawable.affection);
                        break;
                    case 'M':
                        animate.setImageResource(R.drawable.marriage);
                        break;
                    case 'E':
                        animate.setImageResource(R.drawable.enemy);
                        break;
                    case 'S':
                        animate.setImageResource(R.drawable.sibling);
                        break;

                }

                    oa2.start();
                if(!(result.equals("Enemies") || result.equals("Sibling"))) {
                    viewKonfetti.build()
                            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                            .setDirection(0.0, 359.0)
                            .setSpeed(3f, 5f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(1000L)
                            .addShapes(Shape.RECT, Shape.CIRCLE)
                            .addSizes(new Size(12, 5))
                            .setPosition(viewKonfetti.getWidth() / 2, viewKonfetti.getHeight() / 3)
                            .streamFor(100, 3000L);
                }
            }
        });
        oa1.setDuration(6000);
        oa2.setDuration(2000);
        oa1.start();

    }

    public Bitmap getScreenShot() {
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public File store(Bitmap bm, String fileName){
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return file;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void shareImage(String filename){

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I have tried FLAMES App in Play Store. Now its your turn!!");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, I have tried FLAMES for " +op+ " and result is *"+result+"* - Wanna try?? Download FLAMES app and try in Google PlayStore. http://bit.ly/flamesapp");

        // getExternalFilesDir() + "/Pictures" should match the declaration in fileprovider.xml paths
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File photoFile = new File(dirPath, filename);

        //Toast.makeText(getApplicationContext(),photoFile.toString(),Toast.LENGTH_SHORT).show();

        // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "com.codepath.fileprovider",photoFile));
        startActivityForResult(Intent.createChooser(shareIntent, "Share FLAMES Image using"),3);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }


}
