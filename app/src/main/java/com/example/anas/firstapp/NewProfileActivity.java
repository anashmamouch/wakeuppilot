package com.example.anas.firstapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



public class NewProfileActivity extends BaseActivity {

    private String username;
    private String age;
    private String genre;

    private User user;

    /*url to get the json data*/
    private static final String url = "http://wakeuppilot.herokuapp.com/players.json";

    /*JSON Node names*/

    /*Common JSON keys*/
    //private static String TAG_ID = "id";
    //private static String TAG_DATE = "created_at";
    /*Game JSON keys*/
    //private static String TAG_BALL_TOUCHED = "ball_touched";
    //private static String TAG_TOTAL_TOUCHES = "total_touches";
    //private static String TAG_FIRST_TIME = "first_time";
    //private static String TAG_PLAYER_ID = "player_id";
    /*Player JSON keys*/
    private static String TAG_USERNAME = "username";
    private static String TAG_GENRE = "genre";
    private static String TAG_AGE = "age";

    //Create a database where to store the data
    DatabaseHandler db = DatabaseHandler.getInstance(this);

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_nouveau_profile);

        checkRadio();

        findViewById(R.id.creer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean condition = true;
                username = ((EditText) findViewById(R.id.pseudonyme)).getText().toString().trim();
                users = db.findAllUsers();

                for (User u : users) {
                    if (username.equals(u.getUsername())) {
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
                    goToActivity(ProfilesActivity.class);
                }
            }
        });

        findViewById(R.id.inscrit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = "";
                //get all users
                users = db.findAllUsers();
                //send the data to the website wakeuppilot.herokuapp.com
                new SendData().execute(url);

                goToActivity(ProfilesActivity.class);
            }
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_profile;
    }

    public void checkRadio(){
        //Valeurs par default
        genre = "Homme";
        age = "- 40 ans";

        ((RadioGroup)findViewById(R.id.radioGenre)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioMale) {
                    genre = "Homme";
                } else if (checkedId == R.id.radioFemale) {
                    genre = "Femme";
                }
            }
        });

        ((RadioGroup)findViewById(R.id.radioAge)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                    JSONArray array = new JSONArray();


                    if(username.length() != 0){
                        //creating the json object to send the data
                        JSONObject data = new JSONObject();

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
                    JSONObject player = new JSONObject();

                    try {
                        player.put("player", array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("BENZINO", "JSON Object : "+ player.toString());

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
}
