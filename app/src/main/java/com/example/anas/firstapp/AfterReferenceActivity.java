package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anas on 31/7/15.
 */
public class AfterReferenceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title;
    private Button retourProfile;

    private User user ;

    private DatabaseHandler dbHelper;

    private List<Test> tests;

    /*TODO OnBackPressed */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
        //intent.putExtra("KEY", user);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_reference);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        retourProfile = (Button) findViewById(R.id.retour_profile);

        dbHelper = new DatabaseHandler(this);

        user = (User) getIntent().getSerializableExtra("KEY");

        tests = dbHelper.findTestByUser(user.getId());

        Log.d("ANAS", "test "+tests.get(0).getFirstTime());

        title.setText(R.string.toolbar_niveau_reference);


        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        retourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the test
                Intent intent = new Intent(getApplicationContext(), TestNewActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
                intent.putExtra("TESTS", (Serializable) tests);
                Log.d("BENZINO", "starting the intent");
                startActivity(intent);
                finish();
            }
        });

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
