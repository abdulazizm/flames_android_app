package com.fpe.flames;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.mediation.MediationBannerAd;

public class MainActivity extends AppCompatActivity {
    Button submit_button,clr,clr2;
    EditText name_1,name_2;
    String name1,name2,result;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        submit_button= findViewById(R.id.submit);
        clr= findViewById(R.id.clr);
        clr2= findViewById(R.id.clr2);
        name_1 = findViewById(R.id.name1);
        name_2 = findViewById(R.id.name2);

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_1.getText().clear();
            }
        });

        clr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_2.getText().clear();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1 = name_1.getText().toString();
                name2 = name_2.getText().toString();


                result = algorithmFind(name1,name2);
                Intent intent=new Intent(MainActivity.this,Result.class);
                intent.putExtra("result",result);
                intent.putExtra("output",name1+" & "+name2 +" :");

                startActivity(intent);


            }
        });

    }

    public String algorithmFind(String name1, String name2){

        name1 = name1.toLowerCase();
        name2 = name2.toLowerCase();

        StringBuilder sb1 = new StringBuilder(name1);// converting to string builder
        StringBuilder sb2 = new StringBuilder(name2);

        int m=sb1.length();
        int n=sb2.length();
        for(int i=0; i<m;i++)
        {
            for(int j=0; j<n;j++)
            {
                if(sb1.charAt(i) == sb2.charAt(j))
                {
                    sb1.replace(i, i+1, "0"); // replacing matching characters into "0"
                    sb2.replace(j,j+1,"0");
                }
            }
        }

        int x1=0;
        int y1=0;
        String s1="";
        String s2="";
        s1 = sb1.toString();
        s2 = sb2.toString();
        for(int i=0;i<s1.length();i++){ //length of string to remove 0 and find the length
            if(s1.charAt(i)!='0'){
                System.out.print(" "+s1.charAt(i));
                x1 +=1;

            }
        }
        System.out.println();
        System.out.println("First String: "+x1);

        for(int i=0;i<s2.length();i++){
            if(s2.charAt(i)!='0'){
                System.out.print(" "+s2.charAt(i));
                y1 +=1;

            }
        }
        System.out.println();
        System.out.println("Second String: "+y1);


        int x = x1+y1; // total length of remaining characters in both the strings
        System.out.println("Length is: "+x);

        String flames = "flames";
        StringBuilder sb3 = new StringBuilder(flames);

        char flameResult = 0;

        while(sb3.length()!=1)
        {
            int y = x%sb3.length();
            String temp;

            if(y!=0)
            {
                temp = sb3.substring(y)+sb3.substring(0, y-1); // taking substring (counting purpose)

            }
            else
            {
                temp = sb3.substring(0, sb3.length()-1); // taking substring (counting purpose)

            }
            sb3 = new StringBuilder(temp);
            flameResult = sb3.charAt(0);

        }
        System.out.println(flameResult);

        switch(flameResult)
        {
            case 'f':
                return "Friends";
            case 'l':
                return "Love";
            case 'a':
                return "Affection";
            case 'm':
                return "Marriage";
            case 'e':
                return "Enemies";
            case 's':
                return "Sibling";

        }

        return "NULL";
    }

}
