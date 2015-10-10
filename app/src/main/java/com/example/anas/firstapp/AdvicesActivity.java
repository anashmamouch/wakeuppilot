package com.example.anas.firstapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

public class AdvicesActivity extends BaseActivity {

    /*Progress dialog to show when loading JSON data*/
    private ProgressDialog progressDialog;

    /*url to get the json data*/
    private  static final String url = "http://wakeuppilot.herokuapp.com/logs.json";

    /*JSON Node names*/
    //private static String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_BODY = "body";
    private static final String TAG_DATE = "created_at";

    /*ListView where we are going to display the data*/
    private ListView listView;

    String dates[]  ;
    String titles[]  ;
    String bodys[] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = (ListView) findViewById(R.id.listView_advices);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_conseils);

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
                Intent intent = new Intent(AdvicesActivity.this, SingleAdviceActivity.class);

                intent.putExtra(TAG_TITLE, title);
                intent.putExtra(TAG_BODY, body);
                intent.putExtra(TAG_DATE, date);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.retour_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ProfilesActivity.class);
            }
        });

        /*Calling the AsyncTask to get the JSON*/
        new GetAdvices().execute(url);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_advices;
    }

    private class GetAdvices extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvicesActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.veuillez_patientez));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

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

                        BufferedReader reader;

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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result != null){
                try {
                    /*Getting the json Array */
                    JSONArray logs = new JSONArray(result);

                    if(logs.length() != 0){
                        titles = new String[logs.length()];
                        bodys = new String [logs.length()];
                        dates = new String[logs.length()];

                        /*Loop through the JSON Array (All logs)*/
                        /*Show the logs in the reverse order*/
                        for (int i = 0; i < logs.length(); i++) {
                            JSONObject logsObject = logs.getJSONObject(i);

                            //String id = logsObject.getString(TAG_ID);
                            titles[i] = logsObject.getString(TAG_TITLE);
                            bodys[i] = logsObject.getString(TAG_BODY);
                            dates[i] = logsObject.getString(TAG_DATE);

                            Log.d("BENZINO", "DATA : " + titles[0] + bodys[0] + dates[0]);
                            Log.d("BENZINO", "DATA LOGS : " + logs);
                        }
                    }else {
                        titles = new String[1];
                        bodys = new String [1];
                        dates = new String[1];

                        titles[0] = getResources().getString(R.string.pas_conseils);
                        bodys[0] = " ";
                        dates[0] = " ";
                    }
                }catch(JSONException e){
                    titles = new String[1];
                    bodys = new String [1];
                    dates = new String[1];

                    titles[0] = getResources().getString(R.string.pas_conseils);
                    bodys[0] = getResources().getString(R.string.verifier_internet);
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

        public ListAdvicesAdapter(String[] title, String[] body, String[] date) {
            this.title = title;
            this.body = body;
            this.date = date;
        }

        @Override
        public int getCount() {
            return title.length;
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
            //Here, position is the index in the list, the convertView is the view to be
            //recycled (or created), and parent is the ListView itself.

            //Grab the convertView as our row of the ListView
            View row = convertView;

            //If the row is null, it means that we aren't recycling anything - so we have
            //to inflate the layout ourselves.
            ViewHolder holder;
            if(row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_advice_item, parent, false);
                //Now create the ViewHolder
                holder = new ViewHolder();
                //and set its textView field to the proper value
                holder.titleTextView = (TextView) row.findViewById(R.id.title);
                holder.bodyTextView = (TextView) row.findViewById(R.id.body);
                holder.dateTextView = (TextView) row.findViewById(R.id.date);

                //and store it as the 'tag' of our view
                row.setTag(holder);
            }else {
                holder = (ViewHolder) row.getTag();
            }

            int index = title.length - position - 1;

            holder.titleTextView.setText(title[index]);
            holder.bodyTextView.setText(body[index]);
            holder.dateTextView.setText(date[index]);

            return row;
        }
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView bodyTextView;
        TextView dateTextView;
    }

}