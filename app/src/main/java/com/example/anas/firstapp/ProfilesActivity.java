package com.example.anas.firstapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anas on 27/7/15.
 */
public class ProfilesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private Button createNewProfile;

    private DatabaseHandler dbHelper;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        createNewProfile = (Button) findViewById(R.id.nouveau_profile);
        listView = (ListView) findViewById(R.id.listView_profiles);
        title.setText("PROFILES");

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the new profile page
                Intent intent = new Intent(getApplicationContext(), NewProfileActivity.class);
                startActivity(intent);
            }
        });


        dbHelper = new DatabaseHandler(this);

        List<User> users = dbHelper.findAllUsers();
        String[] names = new String[users.size()];
        String[] ages = new String[users.size()];

        for(int i=0; i<users.size(); i++){
            names[i] = users.get(i).getUsername();
            ages[i] = users.get(i).getAge();
        }

        listView.setAdapter(new ListProfilesAdapter(names, ages));

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
        String[] age;

        public ListProfilesAdapter() {
            username = null;
            age = null;
        }

        public ListProfilesAdapter(String[] username, String[] age) {
            this.username = username;
            this.age = age;
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
            View row = inflater.inflate(R.layout.listview_item, parent, false);

            TextView usernameTextView = (TextView) row.findViewById(R.id.textView1);
            TextView ageTextView = (TextView) row.findViewById(R.id.textView2);

            usernameTextView.setText(username[position]);
            ageTextView.setText(age[position]);

            return row;
        }
    }
}
