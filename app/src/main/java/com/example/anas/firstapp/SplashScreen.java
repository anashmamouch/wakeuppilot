package com.example.anas.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



/**
 * Created by Anas on 23/7/15.
 */
public class SplashScreen extends Activity {
    /** Duration of wait */
    private static int SPLASH_TIME_OUT = 3000;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
