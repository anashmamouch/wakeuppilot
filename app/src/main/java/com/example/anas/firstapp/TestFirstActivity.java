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

import com.example.anas.firstapp.test.GameActivity;

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

        user = (User) getIntent().getSerializableExtra("KEY");

        title.setText(user.getUsername());

        //test.setText(user.getUsername()+"\n"+user.getAge()+"\n"+user.getGenre());
        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        passerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the Reference page
                Intent intent = new Intent(getApplicationContext(), ReferenceActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
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
