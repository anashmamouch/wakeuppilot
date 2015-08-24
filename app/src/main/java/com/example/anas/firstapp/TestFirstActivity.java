package com.example.anas.firstapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;

import java.util.Locale;

/**
 * Created by Anas on 31/7/15.
 */
public class TestFirstActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private Button passerTest;
    private Button historiqueResultats;
    private User user ;
    private TextView test;
    private TextView successRate;

    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_test);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        passerTest = (Button) findViewById(R.id.niveau_reference);
        //test = (TextView) findViewById(R.id.test_textview);
        successRate = (TextView) findViewById(R.id.success_rate);
        historiqueResultats = (Button)findViewById(R.id.historique_resultats);

        lang  = (String) getIntent().getSerializableExtra("LANG");

        user = (User) getIntent().getSerializableExtra("KEY");

        String username = user.getUsername();
        String age = user.getAge();
        String genre = user.getGenre();

        if(age.equals("- 40 ans")){
            age = getResources().getString(R.string.moins_40);
        }
        else if(age.equals("40 - 60 ans")){
            age = getResources().getString(R.string.entre_40_60);
        }
        else if(age.equals("+ 60 ans")){
            age = getResources().getString(R.string.plus_60);
        }

        if(genre.equals("Homme")){
            genre = getResources().getString(R.string.male);
        }else if(genre.equals("Femme")){
            genre = getResources().getString(R.string.female);
        }

        if(lang.equals("ar")){
            title.setText(age + " | " + username);
        }else {
            title.setText(username + " | " + age);

        }
        //Setting the toolbar as the ActionBar
        toolbar.setNavigationIcon(R.drawable.logo_white_32);
        //toolbar.setNavigationIcon(R.drawable.back_white);
        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(" ");
        //getSupportActionBar().setLogo(R.drawable.logo_white_32);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        passerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the Reference page
                Intent intent = new Intent(getApplicationContext(), ReferenceActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                Log.d("BENZINO", "starting the intent");
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
                intent.putExtra("LANG", lang);
                setLocale(lang);
                Log.d("BENZINO", "starting the intent");
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        newConfig.locale = new Locale(lang);
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
        super.onConfigurationChanged(newConfig);
    }
    //Conflicts between language and orientation solved.
    public void setLocale(String lang) {

        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(lang);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

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

        //Language selection
        if (id == R.id.Language) {
            startActivity(new Intent(getApplicationContext(), ChooseLang.class));
            finish();
            return true;
        }

        //Map Activity selection
        if (id == R.id.action_map) {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.putExtra("LANG", lang);
            startActivity(intent);
            //finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
