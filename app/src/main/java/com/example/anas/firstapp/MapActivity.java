package com.example.anas.firstapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class MapActivity extends BaseActivity implements LocationListener{
    //the map
    private GoogleMap map;

    //location manager
    private LocationManager locationManager;

    //user marker
    private Marker userMarker;

    //markers for places of interest
    private Marker[] placeMarkers;

    //max places you can display mostly set by google
    private final int MAX_PLACES = 20;

    //marker options
    private MarkerOptions[] places;

    //instance variables for Marker icon drawable
    private int userIcon;
    private int drinkIcon;

    private boolean updateFinished = true;
    private String BROWSER_KEY = "AIzaSyCh-k2YCfbPnKYpK2aqMureow1OP4RFCpk";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_carte);

        //get drawable IDs
        userIcon = R.drawable.marker_orange;
        drinkIcon = R.drawable.marker_teal;

        //find out if we already have it
        if(map==null) {
            //get the map
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            //check in case map/ Google Play services not available
            if (map != null) {
                //ok - proceed
                //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                //create marker array
                placeMarkers = new Marker[MAX_PLACES];

                map.setMyLocationEnabled(true);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
                //update location
                updatePlaces();
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_map;
    }

    //method to update user location
    private void updatePlaces(){
        Log.d("BENZINO MAP", "MAP LOCATION MANAGER : " + locationManager.toString());
        //Location lastLocation = getLastKnownLocation();
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        //Log.d("BENZINO MAP", "MAP LAST LOCATION : " + lastLocation.toString());

        if(lastLocation == null){

            Log.d("BENZINO MAP", "lastLocation is null check the code");
            Toast.makeText(MapActivity.this, "Please turn on your GPS", Toast.LENGTH_SHORT).show();

        }else{
            //save the current latitude and longitude
            double lat  = lastLocation.getLatitude();
            double lng = lastLocation.getLongitude();

            //wrap up in a LatLng object to put it on the marker
            LatLng lastLatLng  = new LatLng(lat, lng);

            //remove any existing markers
            if( userMarker !=null )
                userMarker.remove();

            //create and set markers properties
            userMarker = map.addMarker(new MarkerOptions()
                    .position(lastLatLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.fromResource(userIcon))
                    .snippet("Your last recorded position"));

            //animate the camera to zoom to the users position
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 9.0F), 1000, null);

            //build places search query string
            String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                    "json?location="+lat+","+lng+
                    "&radius=40000&sensor=true" +
                    "&types=gas_station"+
                    "&key="+BROWSER_KEY;

            //execute query
            new GetPlaces().execute(placesSearchStr);
        }
    }

    //Async task classes to fetch the places
    private class GetPlaces extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... placesURL) {
            //fetch and get places data
            updateFinished = false;

            //build result as a string
            StringBuilder placesBuilder = new StringBuilder();

            //process Search parameter string
            for(String placesSearchURL:placesURL){

                //try to fetch the data
                try{

                    URL requestUrl = new URL(placesSearchURL);
                    HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    Log.d("BENZINO", "RESPONSE CODE : " + responseCode);

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
                            placesBuilder.append(line  + "\n");

                        if (placesBuilder.length() == 0)
                            return "";

                        Log.d("BENZINO", "PLACES BUILDER : " + placesBuilder.toString());
                    }else{

                        Log.d("BENZINO", "Unsuccessful HTTP Response Code: " + responseCode);
                    }
                }catch (MalformedURLException e) {
                    Log.d("BENZINO", "Error processing Places API URL", e);
                } catch (IOException e) {
                    Log.d("BENZINO", "Error connecting to Places API", e);
                }
            }
            return placesBuilder.toString();
        }

        //process data from the doInBackground method
        @Override
        protected void onPostExecute(String result) {
            //parse places data returned from doInBackground
            //removing any existing markers
            if(placeMarkers != null) {
                for (int i = 0; i < placeMarkers.length; i++) {
                    if (placeMarkers[i] != null){
                        placeMarkers[i].remove();
                    }
                }
            }


            try{
                //create JSON object parse string returned from doInBackground
                JSONObject resultObject = new JSONObject(result);

                //String pageToken = resultObject.getString("next_page_token");
                //Log.d("BENZINO", "Token : " + pageToken );

                //get "results" Array from resultObject
                JSONArray placesArray = resultObject.getJSONArray("results");
                //array of marker options for each place
                places = new MarkerOptions[placesArray.length()];

                Log.d("BENZINO", "PLACES LENGHT : "+placesArray.length());
                Log.d("BENZINO", "PLACES ARRAY : "+placesArray.toString());
                //loop through places
                for(int i = 0; i < placesArray.length(); i++){
                    //parse each place
                    //if any value is missing we wont show the marker
                    boolean missingValue = false;

                    LatLng placeLatLng = null;
                    //String placeName = "";
                    String placeName = "Station de gas";
                    String vicinity = "";
                    int currentIcon = userIcon;
                    try{
                        //attempt to retrieve place data values
                        missingValue = false;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(i);
                        //get location section
                        JSONObject loc = placeObject.getJSONObject("geometry").getJSONObject("location");
                        //read lat lng
                        placeLatLng = new LatLng(
                                Double.valueOf(loc.getString("lat")),
                                Double.valueOf(loc.getString("lng")));
                        //get types
                        JSONArray types = placeObject.getJSONArray("types");
                        //loop through the type array
                        for(int t = 0; t < types.length(); t++){
                            String type = types.get(t).toString();
                            //using particular icons for each location
                            if(type.contains("gas_station")){
                                Log.d("BENZINO", "GAS");
                                Log.d("GAS", "PLACE GAS : " + placeObject.toString());
                                currentIcon = drinkIcon;
                                break;
                            }
                        }
                        //get vicinity data
                        vicinity = placeObject.getString("vicinity");
                        //place name
                        placeName = placeObject.getString("name");

                    }catch (JSONException ex){
                        Log.d("BENZINO", "missing value");
                        missingValue = true;
                        ex.printStackTrace();
                    }
                    //Show marker
                    places[i] = new MarkerOptions()
                            .position(placeLatLng)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.fromResource(currentIcon))
                            .snippet(vicinity);

                    /**
                     if(missingValue)
                     places[i]=null;
                     else
                     places[i] = new MarkerOptions()
                     .position(placeLatLng)
                     .title(placeName)
                     .icon(BitmapDescriptorFactory.fromResource(currentIcon))
                     .snippet(vicinity);
                     **/
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.d("BENZINO", e.getMessage());

            }
            if(places!=null && placeMarkers!=null){
                for(int p=0; p<places.length && p<placeMarkers.length; p++){
                    //will be null if a value was missing
                    if(places[p]!=null)
                        placeMarkers[p]=map.addMarker(places[p]);
                }
            }
        }

        //Log.d("BENZINO", "onPost");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("BENZINO", "Location Changed");
        updatePlaces();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("BENZINO", "Status Changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("BENZINO", "Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("BENZINO", "Provider Disabled");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(map!=null){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(map!=null){
            locationManager.removeUpdates(this);
        }
    }

}
