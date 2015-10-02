package com.example.anas.firstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.List;

public class TestNewActivity extends BaseActivity{

    private User user ;
    private List<Test> tests;
    private int scoreReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("KEY");

        DatabaseHandler dbHelper = DatabaseHandler.getInstance(this);

        tests = dbHelper.findTestByUser(user.getId());

        int total = tests.size();

        if(total !=0)
            scoreReference = tests.get((total - 1)).getBallTouched();

        success(scoreReference, total);

        String username = user.getUsername();
        String age = user.getAge();

        if(age.equals("- 40 ans")){
            age = getResources().getString(R.string.moins_40);
        }
        else if(age.equals("40 - 60 ans")){
            age = getResources().getString(R.string.entre_40_60);
        }
        else if(age.equals("+ 60 ans")){
            age = getResources().getString(R.string.plus_60);
        }

        if(lang.equals("ar")){
            ((TextView) findViewById(R.id.toolbar_title)).setText(age + " | " + username);
        }else{
            ((TextView) findViewById(R.id.toolbar_title)).setText(username + " | " + age);
        }

        /**Starting DecoView
         *
         *
         */

        DecoView decoView = (DecoView) findViewById(R.id.dynamicArcView);

        //Add the background arc
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 50, 0)
                .build();

        int backIndex  = decoView.addSeries(seriesItem);

        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0,100, 0)
                .build();

        int series1Index = decoView.addSeries(seriesItem1);

        //Adding a listener to monitor the value changes
        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                //float percentFilled =  0.74f;
                ((TextView) findViewById(R.id.textPercentage)).setText(String.format("%.0f%%", percentFilled * 100f));
            }
            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        decoView.addEvent(new DecoEvent.Builder(50)
                .setIndex(backIndex)

                .build());
        /**
         decoView.addEvent(new DecoEvent.Builder(16.3f)
         .setIndex(series1Index)
         .setDelay(5000)
         .build());
         **/
        decoView.addEvent(new DecoEvent.Builder(success(scoreReference, total))
                .setIndex(series1Index)

                .setDelay(3000)
                .build());

        /**Ending DecoView
         *
         *
         */

        findViewById(R.id.passer_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to the test
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("KEY", user);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                startActivity(intent);
                finish();

            }
        });

        findViewById(R.id.retour_list_profiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.historique_resultats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the History Results page
                Intent intent = new Intent(getApplicationContext(), HistoryResultsActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                Log.d("BENZINO", "starting the intent");
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_test;
    }

    public  float success(int scoreReference, int total){
        int[] scores;

        int last = total - 1;
        float sum;
        float somme = 0;

        for(int i=0; i<total; i++){
            Log.d("BENZINO", "inside for loop tests :::::::::::>> "+ tests.get(i).toString());
            if(tests.get(i).getFirstTime()){
                //scoreReference = tests.get(i).getBallTouched();
            }else{
                sum = tests.get(i).getBallTouched();
                Log.d("BENZINO", "inside for loop tests :::::::::::>> "+ i +" - "+ sum);

                //Log.d("ANAS", "somme pour test"sum);
                //if(scoreReference !=0)
                somme += sum/(float)scoreReference;

                Log.d("BENZINO", "inside for loop tests :::::::::::>> SOMME "+ somme);
            }
        }
        if(last > 0){
            somme = somme/last;
            Log.d("BENZINO", "SCORE REFERENCE INSIDE IF  :::::::::::>>>>>>>>>>> "+ somme);
        }

        else
            somme = 1;

        Log.d("BENZINO", "SOMME :::::::::::>>>>>>>>>>> "+ somme);

        return somme*100;
    }

}
