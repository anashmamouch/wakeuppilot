package com.example.anas.firstapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anas.firstapp.DatabaseHandler;
import com.example.anas.firstapp.R;
import com.example.anas.firstapp.Test;
import com.example.anas.firstapp.User;

import java.util.List;

/**
 * Created by Anas on 30/7/15.
 */
public class BestScoreActivity extends AppCompatActivity{


        private DatabaseHandler dbHelper;

        private User user;

        private List<Test> tests;

        private ListView listView;



        private String[] usernames;
        private String[] scores;


        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.best_score);


            listView = (ListView) findViewById(R.id.list_best_score);

            dbHelper = new DatabaseHandler(this);

            user = (User) getIntent().getSerializableExtra("KEY");

            tests = dbHelper.findTestByUser(user.getId());

            Log.d("ANAS", tests.toString());

            usernames = new String[tests.size()];
            scores = new String[tests.size()];

            for(int i=0; i<tests.size(); i++){
                //usernames[i] =  dbHelper.findUserById(tests.get(i).getUserId()).getUsername()  ;
                usernames[i] = tests.get(i).getCreatedAt();
                scores[i] = String.valueOf(tests.get(i).getBallTouched());
            }

            listView.setAdapter(new ListProfilesAdapter(usernames, scores));
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

    class ListProfilesAdapter extends BaseAdapter {

        String[] username;
        String[] score;

        public ListProfilesAdapter() {
            username = null;
            score = null;
        }

        public ListProfilesAdapter(String[] username, String[] score) {
            this.username = username;
            this.score = score;
        }

        @Override
        public int getCount() {
            return username.length;
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

            usernameTextView.setText(username[position]);
            ageTextView.setText(score[position]);

            return row;
        }
    }
}
