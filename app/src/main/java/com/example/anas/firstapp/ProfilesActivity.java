package com.example.anas.firstapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anas on 27/7/15.
 */
public class ProfilesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private Button createNewProfile;

    private DatabaseHandler dbHelper;

    private ListView listView;

    private List<User> users;
    private List<Test> tests;

    private String[] names;
    private String[] ages;

    private User user;

    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        createNewProfile = (Button) findViewById(R.id.nouveau_profile);
        listView = (ListView) findViewById(R.id.listView_profiles);
        title.setText(R.string.toolbar_liste_profiles);

        lang  = (String) getIntent().getSerializableExtra("LANG");

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the new profile page
                Intent intent = new Intent(getApplicationContext(), NewProfileActivity.class);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                startActivity(intent);
                finish();
            }
        });


        dbHelper = new DatabaseHandler(this);

        users = dbHelper.findAllUsers();
        names = new String[users.size()];
        ages = new String[users.size()];

        for(int i=0; i<users.size(); i++){
            names[i] = users.get(i).getUsername();
            ages[i] = users.get(i).getAge();
        }

        listView.setAdapter(new ListProfilesAdapter(names, ages));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getApplicationContext(), names[position], Toast.LENGTH_LONG).show();
                user = users.get(position);

                tests = dbHelper.findTestByUser(user.getId());

                //If the user already passed the test
                if(!tests.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), TestNewActivity.class);
                    Log.d("BENZINO", "creating the intent for the TESTNEWACTIVITY ");
                    intent.putExtra("KEY", user);
                    intent.putExtra("LANG", lang);
                    setLocale(lang);
                    Log.d("BENZINO", "starting the intent for the TESTNEWACTIVITY ");
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), TestFirstActivity.class);
                    Log.d("BENZINO", "creating the intent for the TESTFIRSTACTIVITY");
                    intent.putExtra("KEY", user);
                    intent.putExtra("LANG", lang);
                    setLocale(lang);
                    Log.d("BENZINO", "starting the intent for the TESTFIRSTACTIVITY");
                    startActivity(intent);
                    finish();
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

            String ageString;
            usernameTextView.setText(username[position]);


            if (age[position].equals("- 40 ans")) {
                ageString = getResources().getString(R.string.moins_40);

            }else if(age[position].equals("40 - 60 ans")){
                ageString = getResources().getString(R.string.entre_40_60);

            }else if(age[position].equals("+ 60 ans")) {
                ageString = getResources().getString(R.string.plus_60);

            }else{
                ageString = "NULL";
            }

            ageTextView.setText(ageString);

            return row;
        }
    }
}
