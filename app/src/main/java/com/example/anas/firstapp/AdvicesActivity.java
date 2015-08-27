package com.example.anas.firstapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class AdvicesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title;
    private Button back;

    private String lang;

    /*Progress dialog to show when loading JSON data*/
    private ProgressDialog progressDialog;

    /*url to get the json data*/
    private static String url = "http://wakeuppilot.herokuapp.com/logs.json";

    /*Logs JSON Array*/
    private JSONArray logs = null;

    /*JSON Node names*/
    private static String TAG_ID = "id";
    private static String TAG_TITLE = "title";
    private static String TAG_BODY = "body";
    private static String TAG_DATE = "created_at";

    /*ListView where we are going to display the data*/
    private ListView listView;

    String dates[]  ;
    String titles[]  ;
    String bodys[] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advices);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (Button) findViewById(R.id.retour_profile);
        listView = (ListView) findViewById(R.id.listView_advices);

        lang = (String) getIntent().getSerializableExtra("LANG");

        title.setText(R.string.toolbar_conseils);

        toolbar.setNavigationIcon(R.drawable.logo_white_32);

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        //getSupportActionBar().setLogo(R.drawable.logo_white_32);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        /*List view on item click listener*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Getting values from the selected item */
                String title = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String body = ((TextView) view.findViewById(R.id.body))
                        .getText().toString();
                String date = ((TextView) view.findViewById(R.id.date))
                        .getText().toString();

                /*Starting single log activity*/
                Intent intent = new Intent(getApplicationContext(),
                        SingleAdviceActivity.class);

                intent.putExtra(TAG_TITLE, title);
                intent.putExtra(TAG_BODY, body);
                intent.putExtra(TAG_DATE, date);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
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

        /*Calling the AsyncTask to get the JSON*/
        new GetAdvices().execute(url);



    }

    private class GetAdvices extends AsyncTask<String, Void, String> {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*Show the Progress Dialog*/
            progressDialog = new ProgressDialog(AdvicesActivity.this);
            progressDialog.setMessage("Veuillez patientez ...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
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

            //build result as a string
            StringBuilder LogsBuilder = new StringBuilder();

            //process the Search parameter string
            for(String logSearchURL:params){
                //try to fetch the data
                try{
                    URL requestUrl = new URL(logSearchURL);
                    HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection();
                    connection.setRequestMethod("GET");

                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    //only keep going is the response is okay
                    if(responseCode == HttpURLConnection.HTTP_OK){

                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();

                        if(inputStream == null)
                            return "";

                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        //read a line at a time append to string builder
                        String line ;
                        while ((line= reader.readLine() )!= null)
                            LogsBuilder.append(line  + "\n");

                        if (LogsBuilder.length() == 0)
                            return "";

                        Log.d("BENZINO", "LOGS BUILDER : " + LogsBuilder.toString());
                    }else{

                        Log.d("BENZINO", "Unsuccessful HTTP Response Code: " + responseCode);
                    }

                }catch (MalformedURLException e) {
                    Log.d("BENZINO", "Error processing URL", e);
                } catch (IOException e) {
                    Log.d("BENZINO", "Error connecting to Host", e);
                }
            }
            /*Return this string to use it on PostExecute*/
            return LogsBuilder.toString();
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param result The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result != null){
                try {
                    /*Getting the json Array */
                    logs = new JSONArray(result);

                    titles = new String[logs.length()];
                    bodys = new String [logs.length()];
                    dates = new String[logs.length()];

                    /*Loop through the JSON Array (All logs)*/
                    for (int i = 0; i < logs.length(); i++) {
                        JSONObject logsObject = logs.getJSONObject(i);

                        String id = logsObject.getString(TAG_ID);
                        titles[i] = logsObject.getString(TAG_TITLE);
                        bodys[i] = logsObject.getString(TAG_BODY);
                        dates[i] = logsObject.getString(TAG_DATE);

                        Log.d("BENZINO", "DATA : " + titles[0] + bodys[0] + dates[0]);
                        Log.d("BENZINO", "DATA LOGS : " + logs);

                    }

                }catch(JSONException e){
                    titles = new String[1];
                    bodys = new String [1];
                    dates = new String[1];

                    titles[0] = "Pas de données !";
                    bodys[0] = "Verifier que vous etes connecte a internet";
                    dates[0] = " ";
                    Log.d("BENZINO", "Error Logs JSON : ", e);
                }
            }else{



                Log.e("BENZINO", "Couldn't get any data from the server!");
            }

             /*Dismiss the progress Dialog*/
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            Log.d("BENZINO", "DATA : "+ titles[0]+"\n" + bodys[0] +"\n"+ dates[0]);

             /*Set the adapter to the listView*/
            listView.setAdapter(new ListAdvicesAdapter(titles, bodys, dates));

        }
    }

    class ListAdvicesAdapter extends BaseAdapter {

        String[] title;
        String[] body;
        String[] date;

        public ListAdvicesAdapter() {
            title = null;
            body = null;
            date = null;
        }

        public ListAdvicesAdapter(String[] title, String[] body, String[] date) {
            this.title = title;
            this.body = body;
            this.date = date;
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return title.length;
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_advice_item, parent, false);

            TextView titleTextView = (TextView) row.findViewById(R.id.title);
            TextView bodyTextView = (TextView) row.findViewById(R.id.body);
            TextView dateTextView = (TextView) row.findViewById(R.id.date);

            titleTextView.setText(title[position]);
            bodyTextView.setText(body[position]);
            dateTextView.setText(date[position]);

            return row;
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


        return super.onOptionsItemSelected(item);
    }


}