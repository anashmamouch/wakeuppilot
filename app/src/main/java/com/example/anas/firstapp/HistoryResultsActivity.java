package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;

import java.util.List;

/**
 * Created by Anas on 31/7/15.
 */
public class HistoryResultsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private Button retourProfile;
    private TextView usernameHistoryResults;

    private DatabaseHandler dbHelper;

    private User user;

    private List<Test> tests;

    private ListView listView;



    private String[] dates;
    private String[] scores;

    /*TODO: OnBackPressed for HistoryResults*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHelper = new DatabaseHandler(this);

        user = (User) getIntent().getSerializableExtra("KEY");

        tests = dbHelper.findTestByUser(user.getId());

        Log.d("ANAS", tests.toString());

        /**
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
         **/
        if(!tests.isEmpty()){
            //go to the new profile page
            Intent intent = new Intent(getApplicationContext(), TestNewActivity.class);
            intent.putExtra("KEY", user);
            startActivity(intent);

        }else {
            //go to the new reference page
            Intent intent = new Intent(getApplicationContext(), TestFirstActivity.class);
            intent.putExtra("KEY", user);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_score);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        retourProfile = (Button) findViewById(R.id.retour_profile);
        usernameHistoryResults = (TextView) findViewById(R.id.username_history_results);

        listView = (ListView) findViewById(R.id.list_best_score);

        dbHelper = new DatabaseHandler(this);

        user = (User) getIntent().getSerializableExtra("KEY");

        tests = dbHelper.findTestByUser(user.getId());

        Log.d("ANAS", tests.toString());

        dates = new String[tests.size()];
        scores = new String[tests.size()];

        for(int i=0; i<tests.size(); i++){
            //usernames[i] =  dbHelper.findUserById(tests.get(i).getUserId()).getUsername()  ;
            dates[i] = tests.get(i).getCreatedAt();
            scores[i] = String.valueOf(tests.get(i).getBallTouched());
        }

        listView.setAdapter(new ListResultsAdapter(dates, scores));

        title.setText("HISTORIQUE DES RESULTATS");
        usernameHistoryResults.setText(user.getUsername());

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        retourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tests.isEmpty()){
                    //go to the new profile page
                    Intent intent = new Intent(getApplicationContext(), TestNewActivity.class);
                    intent.putExtra("KEY", user);
                    startActivity(intent);

                }else {
                    //go to the new reference page
                    Intent intent = new Intent(getApplicationContext(), TestFirstActivity.class);
                    intent.putExtra("KEY", user);
                    startActivity(intent);

                }

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

    class ListResultsAdapter extends BaseAdapter {

        String[] date;
        String[] score;

        public ListResultsAdapter() {
            date = null;
            score = null;
        }

        public ListResultsAdapter(String[] username, String[] score) {
            this.date = username;
            this.score = score;
        }

        @Override
        public int getCount() {
            return date.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.best_score_item, parent, false);

            TextView usernameTextView = (TextView) row.findViewById(R.id.text_date);
            TextView ageTextView = (TextView) row.findViewById(R.id.text_score);

            usernameTextView.setText(date[position]);
            ageTextView.setText(score[position]);

            return row;
        }
    }
}
