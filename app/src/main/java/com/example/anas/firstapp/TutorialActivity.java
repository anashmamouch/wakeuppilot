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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;

import java.util.Locale;

/**
 * Created by Anas on 31/7/15.
 */
public class TutorialActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title;
    private Button jaiCompris;
    private User user ;

    private String lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        jaiCompris = (Button) findViewById(R.id.jai_compris_button);


        lang = (String) getIntent().getSerializableExtra("LANG");

        title.setText(R.string.toolbar_tutoriel);
        toolbar.setNavigationIcon(R.drawable.logo_white_32);
        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        title.setText(R.string.toolbar_tutoriel);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //redballImage.setBackgroundResource(R.drawable.redball);

        user = (User) getIntent().getSerializableExtra("KEY");


        jaiCompris.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        Log.d("BENZINO", "language = "+ lang);

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
