package com.example.anas.firstapp.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anas.firstapp.AfterReferenceActivity;
import com.example.anas.firstapp.DatabaseHandler;
import com.example.anas.firstapp.HistoryResultsActivity;
import com.example.anas.firstapp.R;
import com.example.anas.firstapp.Test;
import com.example.anas.firstapp.TestFirstActivity;
import com.example.anas.firstapp.TestNewActivity;
import com.example.anas.firstapp.User;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Anas on 30/7/15.
 */
public class AnimationView extends SurfaceView implements SurfaceHolder.Callback{

    /**
     *  @x the coordinate on the x axis
     *  @y the coordinate on the y axis
     *  @vx the velocity on the x axis
     *  @vy the velocity on the y axis
     *  @width the width of the canvas
     *  @height the height of the canvas
     *  @circleRadius the radius of the circle
     *  @circlePaint
     *  @touching variable to check if the user is touching the screen
     *  @loop instance of the Loop class
     */

    private float x;
    private float y;

    private float vx;
    private float vy;

    private float width;
    private float height;

    private float circleRadius;

    private Paint circlePaint;
    private Paint textPaint;
    private Paint timePaint;

    private boolean touching = false;

    private int time = 0 ;

    private float rate = 0  ;
    private int touched = 0;
    private int touch = 0;
    private Loop loop;

    private EditText usernameEditText;

    /**
     * Calls the super() method to give us our surfaceView to work with
     * Link the class up with the SurfaceHolder.Callback
     * Initialize variables regarding the circle
     * Set the speed of mouvement in each direction
     * @param context
     */
    public AnimationView(Context context) {
        super(context);
        getHolder().addCallback(this);
        getHolder().setKeepScreenOn(true);

        circleRadius = 30;
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(16);

        timePaint = new Paint();
        timePaint.setColor(Color.BLUE);
        timePaint.setTextAlign(Paint.Align.CENTER);
        timePaint.setTextSize(22);


        vx = 2;
        vy = 2;
    }

    /**
     *
     * Getters and Setters
     */
    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public int getTouched() {
        return touched;
    }

    public void setTouched(int touched) {
        this.touched = touched;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }


    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isTouching() {
        return touching;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Loop getLoop() {
        return loop;
    }

    public void setLoop(Loop loop) {
        this.loop = loop;
    }

    public void incrementTime(int count){
        this.time +=count;
    }

    public void setTouching(boolean touching) {
        this.touching = touching;
    }

    public void success(){
        if(touch != 0)
            rate = ((touched/touch)*100);
    }

    public void increaseSpeed(float count){
        if(vx > 0)
            vx +=count;
        else
            vx -= count ;

        if(vy > 0)
            vy +=count;
        else
            vy -= count ;

        touched++;
    }

    public void decreaseSpeed(float count){
        if(vx > 0)
            vx -=count;
        else
            vx += count ;

        if(vy > 0)
            vy -=count;
        else
            vy += count ;
    }

    public void touchedBall(int z){

    }



    public void changePosition(){
        Random xRand = new Random();
        Random yRand = new Random();

        x = xRand.nextInt(1000);
        y = yRand.nextInt(1000);

        vx*= -1;
        vy*= -1;
    }

    public void incrementTouch(){
        touch++;
        rate = (touched/(float)touch)*100f;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        canvas.drawText("Time in seconds: " + time, width / 2, 30, timePaint);
        canvas.drawText("Touched: "+touching, 0, 50, textPaint);
        canvas.drawText("POSITION X = " + x + "; Y = " + y , 0, 70, textPaint);
        canvas.drawText("VELOCITY VX = "+String.format("%.2f",vx)+"; VY = "+String.format("%.2f", vy)+"",0, 90, textPaint);
        canvas.drawText("SCREEN SIZE Width = "+getWidth()+"; Height = "+getHeight()+".", 0, 110, textPaint);
        canvas.drawText("BALL TOUCHED " + touched + " times" , 0, 130, textPaint);
        canvas.drawText("TOTAL TOUCHES: " + touch + " times" , 0, 150, textPaint);
        canvas.drawText("SUCCESS RATE: " + rate + " %" , 0, 170, textPaint);
        canvas.drawCircle(x, y, circleRadius, circlePaint);
    }

    /**
     * Handles the simple physics of the mouvement
     * Update the position of the ball
     * Make the ball bounce if it hits the edges of the canvas
     */
    public void updatePhysics(){
        x += vx;
        y += vy;

        if(y - circleRadius < 0 || y + circleRadius > height){
            //The ball has hit the top or the bottom of the canvas
            if(y - circleRadius < 0){
                //The ball has hit the top of the canvas
                y = circleRadius;
            }else{
                //The ball has hit the bottom of the canvas
                y = height - circleRadius;
            }
            //Reverse the y direction of the ball
            vy *= -1;
        }

        if(x - circleRadius < 0 || x + circleRadius > width){
            //The ball has hit the sides of the canvas
            if(x - circleRadius <0){
                //The ball has hit the left side of the canvas
                x = circleRadius;
            }else{
                //The ball has hit the right side of the canvas
                x = width - circleRadius;
            }
            //Reverse the x direction of the ball
            vx *= -1;
        }
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Rect surface = holder.getSurfaceFrame();
        width  = surface.width();
        height = surface.height();

        x = width/2;
        y = circleRadius;

        loop = new Loop(this);
        loop.isRunning(true);
        loop.start();

        Log.d("ANAS", "++++++++++++++SURFACE CREATED    LOOP: "  +  loop.getState());

    }
    //Conflicts between language and orientation solved.
    public void setLocale(String lang) {

        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(lang);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

    }

    public void alertDialog(final User user, final Context context, final String lang){

        final DatabaseHandler db = new DatabaseHandler(context);
        final List<Test>  tests = db.findTestByUser(user.getId());

        String touchezBall = getResources().getString(R.string.dialog_touchez_balle);
        String fois = getResources().getString(R.string.dialog_fois);
            /*
         * Alert Dialog
         * */

        if(time  == 30 && touched > 5){
            /*
             * Inflate the XML view. activity_main is in
             * res/layout/form_elements.xml
             */

            //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //final View inputView = inflater.inflate(R.layout.input, null, false);

            //usernameEditText = (EditText) inputView.findViewById(R.id.inputEditText);

            //Create a database where to store the data


            final AlertDialog alert = new AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_title)
                    .setMessage(touchezBall  +" " +  touched  +" "+ fois)
                    .setPositiveButton(R.string.dialog_refaire_test, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            time = 0;
                            touched = 0;
                            touch = 0;
                            vx = 2;
                            vy = 2;
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.dialog_enregistrer_score, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            //String username = usernameEditText.getText().toString();

                                Log.d("ANAS", "Inserting-----------------------------------");

                                Log.d("ANAS", "USERNAME:" + user.getUsername());
                                Log.d("Anas", "Score: " + touched);
                                //Log.d("ANAS", "tests: "+tests.toString());

                                if(tests.isEmpty()){
                                    /**TODO */
                                    db.createTest(new Test(touched, touch, true, user.getId()));

                                    List<Test> testsCreated = db.findTestByUser(user.getId());

                                    Log.d("ANAS", "INSIDE DIALOG: FIRST TIME" + testsCreated.get(0).getFirstTime());
                                    //go to the list of best scores page
                                    Intent intent = new Intent(context, AfterReferenceActivity.class);
                                    intent.putExtra("KEY", user);
                                    intent.putExtra("LANG", lang);
                                    setLocale(lang);
                                    context.startActivity(intent);
                                    //((Activity) context).finish();
                                    //Interupting the Game Loop
                                    loop.interrupt();
                                    //Dismisses the dialog box
                                    dialog.dismiss();

                                }else{
                                    /**TODO */
                                    db.createTest(new Test(touched, touch, false, user.getId()));

                                    //go to the list of best scores page
                                    Intent intent = new Intent(context, HistoryResultsActivity.class);
                                    intent.putExtra("KEY", user);
                                    intent.putExtra("LANG", lang);
                                    setLocale(lang);
                                    context.startActivity(intent);
                                    //((Activity) context).finish();
                                    //Interupting the Game Loop
                                    loop.interrupt();
                                    //Dismisses the dialog box
                                    dialog.dismiss();
                                }


                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            alert.setCanceledOnTouchOutside(false);

        }

    /*
     * Finish Alert Dialog
     * */
        //if the user touched the ball less than 3 times
        if (time ==30 && touched <= 5){

            AlertDialog alert = new AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_score_minimum)
                    .setPositiveButton(R.string.dialog_refaire_test, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // redo the test
                            time = 0;
                            touched = 0;
                            touch = 0;
                            vx = 2;
                            vy = 2;
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.dialog_retourner_profile, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if(tests.isEmpty()){
                                // go back to the profile page
                                Intent intent = new Intent(context, TestFirstActivity.class);
                                intent.putExtra("KEY", user);
                                intent.putExtra("LANG", lang);
                                setLocale(lang);
                                context.startActivity(intent);
                                //((Activity) context).finish();
                                loop.interrupt();
                                dialog.dismiss();
                            }else{
                                // go back to the profile page
                                Intent intent = new Intent(context, TestNewActivity.class);
                                intent.putExtra("KEY", user);
                                intent.putExtra("LANG", lang);
                                setLocale(lang);
                                context.startActivity(intent);
                                //((Activity) context).finish();
                                loop.interrupt();
                                dialog.dismiss();

                            }

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            alert.setCanceledOnTouchOutside(false);

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // simply copied from sample application LunarLander:
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode

        boolean retry = true;
        loop.isRunning(false);
        while (retry) {
            try {
                loop.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }

    }
}
