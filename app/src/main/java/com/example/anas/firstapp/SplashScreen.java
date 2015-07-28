package com.example.anas.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Anas on 23/7/15.
 */
public class SplashScreen extends Activity {
    /** Duration of wait */
    private static int SPLASH_TIME_OUT = 3000;
    private ImageView splashImg;
    private TextView splashText;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImg = (ImageView) findViewById(R.id.splash_image);
        splashText = (TextView) findViewById(R.id.splash_text);

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "lobster.ttf");
        splashText.setTypeface(font);
         /*
         * New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.
         * */
        new Handler().postDelayed(new Runnable(){

            /*
             * the run method will be executed once the timer ends
             * stating the WelcomeActivity
             * */
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));

                //close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
