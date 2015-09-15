package com.example.anas.firstapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private User user;

    /*url to get the json data*/
    private static String url = "http://wakeuppilot.herokuapp.com/players.json";

    /*Logs JSON Array*/
    private JSONObject data = null;
    private JSONObject player = null;
    private JSONArray array = null;

    /*JSON Node names*/
    /*Common JSON keys*/
    private static String TAG_ID = "id";
    private static String TAG_DATE = "created_at";
    /*Game JSON keys*/
    private static String TAG_BALL_TOUCHED = "ball_touched";
    private static String TAG_TOTAL_TOUCHES = "total_touches";
    private static String TAG_FIRST_TIME = "first_time";
    private static String TAG_PLAYER_ID = "player_id";
    /*Player JSON keys*/
    private static String TAG_USERNAME = "username";
    private static String TAG_GENRE = "genre";
    private static String TAG_AGE = "age";

    //Create a database where to store the data
    final DatabaseHandler db = new DatabaseHandler(this);

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

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
                boolean condition = true;
                username = pseudonyme.getText().toString().trim();

                users = db.findAllUsers();

                for(User u: users){
                    if(username.equals(u.getUsername())){
                        Toast.makeText(getApplicationContext(), "Ce nom existe deja", Toast.LENGTH_LONG).show();
                        condition = false;
                        break;
                    }

                }

                if (username.length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.toast_entrer_pseudonyme, Toast.LENGTH_LONG).show();
                } else if (username.length() > 24) {
                    Toast.makeText(getApplicationContext(), R.string.toast_depasser_pseudonyme, Toast.LENGTH_LONG).show();
                } else if (condition) {
                    //create the user with the data selected
                    user = new User(username, age, false, genre);

                    //persist the user in the database
                    db.createUser(user);

                    //send the data to the website wakeuppilot.herokuapp.com
                    new SendData().execute(url);

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
                username = "";
                //get all users
                users = db.findAllUsers();
                //send the data to the website wakeuppilot.herokuapp.com
                new SendData().execute(url);

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

    /*TODO TRACKING USERS CLASS*/
    private class SendData extends AsyncTask<String, Void, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {

            //process the Search parameter string
            for (String sendUrl : params) {
                //try to fetch the data
                try {
                    URL requestUrl = new URL(sendUrl);
                    HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.connect();

                    //creating the array
                    array = new JSONArray();


                    if(username.length() != 0){
                        //creating the json object to send the data
                        data = new JSONObject();

                        try {
                            data.put(TAG_USERNAME, username);
                            data.put(TAG_AGE, age);
                            data.put(TAG_GENRE, genre);

                        } catch (JSONException e) {
                            Log.d("BENZINO", "Error handling JSON Object", e);
                        }

                        array.put(data);
                        //when data is sent set sent to true
                        user = db.findUserByName(username);
                        user.setSent(true);
                        //update user in the database
                        db.updateUser(user);
                    }

                    //Check if there is still users that are not registred
                    for(User u: users){

                        if(u.isSent() == false){
                            Log.d("BENZINO DATABASE ", "SEND DATA : " + u.getUsername() + " >> " + u.isSent());

                            JSONObject json = new JSONObject();
                            try {
                                json.put(TAG_USERNAME, u.getUsername());
                                json.put(TAG_AGE, u.getAge());
                                json.put(TAG_GENRE, u.getGenre());
                                array.put(json);
                                Log.d("BENZINO", "JSON ARRAY: " + array.toString());

                            } catch (JSONException e) {
                                Log.d("BENZINO", "Error handling JSON Object", e);
                            }

                            u.setSent(true);
                            Log.d("BENZINO", "USER: " + u.toString());
                            db.updateUser(u);
                        }
                    }
                    //the main json object that contains the array that we will send
                    player = new JSONObject();

                    try {
                        player.put("player", array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("BENZINO", "JSON Object : "+player.toString());

                    //sending data & specifying the encoding utf-8
                    OutputStream os = connection.getOutputStream();
                    os.write(player.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    //display what returns the POST request
                    StringBuilder sb = new StringBuilder();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {


                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                        String line ;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        br.close();

                        Log.d("BENZINO", "HTTP POST Response : " + sb.toString());


                    } else {

                        Log.d("BENZINO", "HTTP POST Response Message : " + connection.getResponseMessage());
                    }

                    Log.d("BENZINO", "HTTP Response Code: " + responseCode);

                } catch (MalformedURLException e) {
                    Log.d("BENZINO", "Error processing URL", e);
                } catch (IOException e) {
                    Log.d("BENZINO", "Error connecting to Host", e);
                }
            }

            return null;
        }

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

        //Advice Activity selection
        if (id == R.id.action_advice) {
            Intent intent = new Intent(getApplicationContext(), AdvicesActivity.class);
            intent.putExtra("LANG", lang);
            startActivity(intent);
            //finish();
            return true;
        }

        //Credits Activity selection
        if (id == R.id.action_credits) {
            Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
            intent.putExtra("LANG", lang);
            startActivity(intent);
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
