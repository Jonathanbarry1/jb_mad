package com.example.yx_jo.dublinbikesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String NAME_TAG = "name";
    private static final String ADDRESS_TAG = "address";
    private static final String LATITUDE_TAG = "lat";
    private static final String LONGITUDE_TAG = "lng";
    private static final String POSITION_TAG = "position";
    private static final String NUMBER_TAG = "number";
    private static final String BIKESAVAIL_TAG ="available_bike_stands";
    private static final String BIKESLEFT_TAG ="available_bikes";
    private ArrayList<BikeStation> bikeStations = new ArrayList<>();
    private FirebaseAuth firebaseAuth;

    private TextView textViewChoose;

    private boolean start = true;
    public String startPoint;
    public String endPoint;
    public LatLng startlatlng;
    public LatLng endlatlng;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewChoose = (TextView) findViewById(R.id.textViewChoose);
        textViewChoose.setText(R.string.StationChooseStart);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new MarkerTask().execute();
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(start){
            //on first click the user selects the marker they began at
            Toast.makeText(this, "Start point selected",
                    Toast.LENGTH_SHORT).show();
            start = false;
            startPoint = marker.getTitle();
            startlatlng = marker.getPosition();
            textViewChoose.setText(R.string.StationChooseEnd);

        }
        else{
            //second click is the ending station and the data is bundled up and sent back to add journey activity
            Toast.makeText(this, "End point selected",
                    Toast.LENGTH_SHORT).show();
            endPoint = marker.getTitle();
            endlatlng = marker.getPosition();

            Intent intent= getIntent();
            Bundle args = new Bundle();
            args.putParcelable("from_position", startlatlng);
            args.putParcelable("to_position", endlatlng);
            intent.putExtra("bundle", args);
            intent.putExtra("startPointName", startPoint);
            intent.putExtra("endPointName", endPoint);
            setResult(RESULT_OK, intent);
            finish();
        }


    }
    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    // Process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.updateProfile:
                startActivity(new Intent(this, UpdateInfo.class));
                return true;
            case R.id.viewJourneys:
                startActivity(new Intent(this, ViewJourneysActivity.class));
                return true;
            case R.id.addJourney:
                startActivity(new Intent(this, AddJourneyActivity.class));
                return true;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return false;
        }
    }

    private class MarkerTask extends AsyncTask<Void, Void, String> {

        //used to populate the markers via JSON data scraped from dublin bikes API

        private static final String LOG_TAG = "DublinBikesApp";

        private static final String SERVICE_URL = "https://api.jcdecaux.com/vls/v1/stations?contract=dublin&apiKey=a41b136a6f07f266a33108c91347fe0ee32f3e23";

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(Void... args) {

            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {
                // Connect to the web service
                URL url = new URL(SERVICE_URL);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Read the JSON data into the StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to service", e);
                //throw new IOException("Error connecting to service", e); //uncaught
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return json.toString();
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String json) {

            try {
                // De-serialize the JSON string into an array of bike station objects
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    String number = jsonObj.getString(NUMBER_TAG);
                    JSONObject positionObject = jsonObj.getJSONObject(POSITION_TAG);
                    Double latitude = positionObject.getDouble(LATITUDE_TAG);
                    Double longitude = positionObject.getDouble(LONGITUDE_TAG);
                    String name = jsonObj.getString(NAME_TAG);
                    String address = jsonObj.getString(ADDRESS_TAG);
                    String availSpots = jsonObj.getString(BIKESAVAIL_TAG);
                    String bikesLeft = jsonObj.getString(BIKESLEFT_TAG);



                    BikeStation bikeStation = new BikeStation(name,address, latitude, longitude, number, availSpots,
                            bikesLeft);

                    bikeStations.add(bikeStation);

                    LatLng latLng = new LatLng(latitude, longitude);

                    if (i == 0) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(13).build();

                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }


                    // Create a marker for each bike station in the JSON data.
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title(name)
                            .snippet(availSpots + " free spots and " + bikesLeft + " bikes available")
                            .position(latLng) );

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker arg0) {

                            View v = getLayoutInflater().inflate(R.layout.custom_layout, null);

                            TextView tLocation = (TextView) v.findViewById(R.id.stationName);

                            TextView tSnippet = (TextView) v.findViewById(R.id.bikeDetails);

                            tLocation.setText(arg0.getTitle());

                            tSnippet.setText(arg0.getSnippet());

                            return v;

                        }
                    });


                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error processing JSON", e);
            }

        }
    }
}
