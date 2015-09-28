package com.example.anas.firstapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
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
    //private ImageView avatar;

    private DatabaseHandler dbHelper;

    private ListView listView;

    private List<User> users;
    private List<Test> tests;

    private String[] names;
    private String[] ages;
    private String[] genres;

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
        //avatar = (ImageView) findViewById(R.id.avatar);
        title.setText(R.string.toolbar_liste_profiles);

        lang  = (String) getIntent().getSerializableExtra("LANG");

        toolbar.setNavigationIcon(R.drawable.logo_white_32);
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


        dbHelper = DatabaseHandler.getInstance(this);

        users = dbHelper.findAllUsers();
        names = new String[users.size()];
        ages = new String[users.size()];
        genres = new String[users.size()];

        for(int i=0; i<users.size(); i++){
            names[i] = users.get(i).getUsername();
            ages[i] = users.get(i).getAge();
            genres[i] = users.get(i).getGenre();
        }

        listView.setAdapter(new ListProfilesAdapter(names, ages, genres));

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
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        config.locale = new Locale(lang);
        getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
            return true;
        }


        //Advice Activity selection
        if (id == R.id.action_advice) {
            Intent intent = new Intent(getApplicationContext(), AdvicesActivity.class);
            intent.putExtra("LANG", lang);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
            return true;
        }

        //Credits Activity selection
        if (id == R.id.action_credits) {
            Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
            intent.putExtra("LANG", lang);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

     class ListProfilesAdapter extends BaseAdapter {

         String[] username;
         String[] age;
         String[] genre;

        public ListProfilesAdapter() {
            username = null;
            age = null;
            genre = null;

        }

        public ListProfilesAdapter(String[] username, String[] age, String[] genre) {
            this.username = username;
            this.age = age;
            this.genre = genre;
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

            ImageView avatar = (ImageView) row.findViewById(R.id.avatar);

            ImageView delete = (ImageView) row.findViewById(R.id.delete_profile);
            //ImageView edit = (ImageView) row.findViewById(R.id.edit_profile);

            String ageString;
            final String usernameString = username[position];
            usernameTextView.setText(usernameString);

            final DatabaseHandler dbHandler = DatabaseHandler.getInstance(getApplicationContext());

            delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   final AlertDialog alert = new AlertDialog.Builder(ProfilesActivity.this)
                           .setTitle(R.string.supprimer_profile)
                           .setMessage(R.string.sure_supprimer_profile)
                           .setPositiveButton(R.string.non, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   // redo the test
                                   dialog.dismiss();
                               }
                           })
                           .setNegativeButton(R.string.oui, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dbHandler.deleteUser(dbHandler.findUserByName(usernameString));

                                   Intent intent = getIntent();
                                   intent.putExtra("LANG", lang);
                                   setLocale(lang);
                                   finish();
                                   startActivity(intent);
                                   dialog.dismiss();
                               }
                           })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();

                   alert.setCanceledOnTouchOutside(false);
               }
           });

            /**
             *
             edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "EDIT BUTTON", Toast.LENGTH_SHORT).show();
                }
            });

             **/


            if (age[position].equals("- 40 ans")) {
                ageString = getResources().getString(R.string.moins_40);

                if(genre[position].equals("Femme"))
                    avatar.setImageResource(R.drawable.avatar_female_40);
                else if(genre[position].equals("Homme"))
                    avatar.setImageResource(R.drawable.avatar_male_40);

            }else if(age[position].equals("40 - 60 ans")){
                ageString = getResources().getString(R.string.entre_40_60);

                if(genre[position].equals("Femme"))
                    avatar.setImageResource(R.drawable.avatar_female_40_60);
                else if(genre[position].equals("Homme"))
                    avatar.setImageResource(R.drawable.avatar_male_40_60);

            }else if(age[position].equals("+ 60 ans")) {
                ageString = getResources().getString(R.string.plus_60);

                if(genre[position].equals("Femme"))
                    avatar.setImageResource(R.drawable.avatar_female_60);
                else if(genre[position].equals("Homme"))
                    avatar.setImageResource(R.drawable.avatar_male_60);

            }else{
                ageString = "NULL";
                avatar.setImageResource(R.drawable.avatar_unknown);
            }

            ageTextView.setText(ageString);

            return row;
        }
    }
}
