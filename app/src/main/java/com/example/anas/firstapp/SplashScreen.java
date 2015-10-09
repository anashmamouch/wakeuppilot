package com.example.anas.firstapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
    /** Duration of wait in ms*/
    private static int SPLASH_TIME_OUT = 3000;

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
                    startActivity(new Intent(SplashScreen.this, ChooseLang.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
    }
}
