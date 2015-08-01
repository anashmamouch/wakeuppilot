package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Anas on 26/7/15.
 */
public class NewProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private EditText pseudonyme;
    private Spinner spinnerAge;
    private Spinner spinnerGenre;
    private Button creer;
    private Button inscrit;

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
        spinnerAge = (Spinner) findViewById(R.id.spinner_age);
        spinnerGenre = (Spinner) findViewById(R.id.spinner_genre);
        creer = (Button) findViewById(R.id.creer);
        inscrit = (Button)findViewById(R.id.inscrit);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterAge = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterGenre = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerAge.setAdapter(adapterAge);
        spinnerGenre.setAdapter(adapterGenre);

        username = pseudonyme.getText().toString();
        age = spinnerAge.getSelectedItem().toString();
        genre = spinnerGenre.getSelectedItem().toString();

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = pseudonyme.getText().toString();
                age = spinnerAge.getSelectedItem().toString();
                genre = spinnerGenre.getSelectedItem().toString();

                if(username.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Vous devez entrer un pseudonyme", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("BENZINO", "Inserting-----------------------------------");

                    Log.d("BENZINO", "USERNAME:" + username);
                    Log.d("BENZINO", "AGE:" + age);
                    Log.d("BENZINO", "GENRE:" + genre);
                    db.createUser(new User(username, age, genre));

                    //go to the list of profiles page
                    Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                    startActivity(intent);
                }
            }
        });

        inscrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                startActivity(intent);
            }
        });

        //Log.d("ANAS", "Inserting ..");
        //db.createUser(new User(username, age, genre));

        title.setText("NOUVEAU PROFILE");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.plus_circle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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