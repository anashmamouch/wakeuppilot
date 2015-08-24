package com.example.anas.firstapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Anas on 26/7/15.
 */
public class NewProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;

    private EditText pseudonyme;

    private RadioGroup radioAge;
    private RadioGroup radioGenre;

    private Button creer;
    private Button inscrit;

    private String lang;

    private String username;
    private String age;
    private String genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        //Create a database where to store the data
        final DatabaseHandler db = new DatabaseHandler(this);


        //Attaching the layout elements to the java object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        pseudonyme = (EditText) findViewById(R.id.pseudonyme);

        creer = (Button) findViewById(R.id.creer);
        inscrit = (Button)findViewById(R.id.inscrit);

        radioAge = (RadioGroup) findViewById(R.id.radioAge);
        radioGenre = (RadioGroup) findViewById(R.id.radioGenre);


        username = pseudonyme.getText().toString();

        checkRadio();

        lang  = (String) getIntent().getSerializableExtra("LANG");

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = pseudonyme.getText().toString().trim();

                if (username.length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.toast_entrer_pseudonyme, Toast.LENGTH_LONG).show();
                } else if (username.length() > 24) {
                    Toast.makeText(getApplicationContext(), R.string.toast_depasser_pseudonyme, Toast.LENGTH_LONG).show();
                } else {

                    db.createUser(new User(username, age, genre));

                    //go to the list of profiles page
                    Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                    intent.putExtra("LANG", lang);
                    setLocale(lang);
                    startActivity(intent);
                    finish();

                }
            }
        });

        inscrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                startActivity(intent);
                finish();
            }
        });

        title.setText(R.string.toolbar_nouveau_profile);

        toolbar.setNavigationIcon(R.drawable.logo_white_32);
        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void checkRadio(){
        //Valeurs par default
        genre = "Homme";
        age = "- 40 ans";

        radioGenre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioMale) {
                    genre = "Homme";
                } else if (checkedId == R.id.radioFemale) {
                    genre = "Femme";
                }
            }
        });

        radioAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_40) {
                    age = "- 40 ans";
                } else if (checkedId == R.id.radio_40_60) {
                    age = "40 - 60 ans";
                } else if (checkedId == R.id.radio_60) {
                    age = "+ 60 ans";
                }
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
