package com.example.anas.firstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Anas on 27/7/15.
 */
public class TestNewActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TextView title;
    private Button passerTest;
    private Button historiqueResultats;
    private Button listProfiles;
    private TextView test;
    private TextView successRate;
    private User user ;
    private List<Test> tests;
    private Test niveauReference;
    private DatabaseHandler dbHelper;
    private int scoreReference;

    private DecoView decoView;
    private TextView textPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_test);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        passerTest = (Button) findViewById(R.id.passer_test);
        //test = (TextView) findViewById(R.id.test_textview);
        historiqueResultats = (Button) findViewById(R.id.historique_resultats);
        listProfiles = (Button)findViewById(R.id.retour_list_profiles);
        //successRate = (TextView) findViewById(R.id.success_rate);

        user = (User) getIntent().getSerializableExtra("KEY");

        dbHelper = new DatabaseHandler(this);

        tests = dbHelper.findTestByUser(user.getId());

        int total = tests.size();

        scoreReference = tests.get((total - 1)).getBallTouched();
        
        success(scoreReference, total);

        //successRate.setText("" + success(scoreReference, total));

        title.setText(user.getUsername() + "("+user.getAge()+")");

        /**Starting DecoView
         *
         *
         */

        decoView = (DecoView) findViewById(R.id.dynamicArcView);

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
        textPercentage = (TextView) findViewById(R.id.textPercentage);
        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                //float percentFilled =  0.74f;
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
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

        //test.setText(user.getUsername() + "\n" + user.getAge() + "\n" + user.getGenre());
        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        passerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to the test
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
                Log.d("BENZINO", "starting the intent");
                startActivity(intent);
                finish();

            }
        });

        listProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        historiqueResultats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the History Results page
                Intent intent = new Intent(getApplicationContext(), HistoryResultsActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
                Log.d("BENZINO", "starting the intent");
                startActivity(intent);
                finish();
            }
        });

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
        //Log.d("BENZINO", "FIRST TIME : " +tests.get(total).getFirstTime());
        if(last > 0){
            somme = somme/last;
            Log.d("BENZINO", "SCORE REFERENCE INSIDE IF  :::::::::::>>>>>>>>>>> "+ somme);
        }

        else
            somme = 1;

        //Log.d("BENZINO", "SCORE REFERENCE :::::::::::>>>>>>>>>>> "+ somme);
        Log.d("BENZINO", "SOMME :::::::::::>>>>>>>>>>> "+ somme);

        return somme*100;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
