package com.example.yx_jo.question1;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in London
        LatLng kingslanding = new LatLng(51.515419, -0.141099);
        final Marker kingslanding_marker = mMap.addMarker(new MarkerOptions().position(kingslanding).title("Kings Landing"));


        // Add a marker in Bath
        LatLng highgarden = new LatLng(51.3801748, -2.3995494);
        final Marker highgarden_marker = mMap.addMarker(new MarkerOptions().position(highgarden).title("HighGarden"));


        // Add a marker in york
        LatLng winterfell = new LatLng(53.9586419, -1.115611);
        final Marker winterfell_marker = mMap.addMarker(new MarkerOptions().position(winterfell).title("Winterfell"));

        // Add a marker at Hardrian's wall
        LatLng theWall = new LatLng(54.9899016,-2.6717341);
        final Marker theWall_marker = mMap.addMarker(new MarkerOptions().position(theWall).title("The Wall"));


        //adding a marker at Birmingham
        LatLng riverrun = new LatLng(52.477564, -2.003715);
        final Marker riverrun_marker = mMap.addMarker(new MarkerOptions().position(riverrun).title("Riverrun"));

        LatLng britain = new LatLng(55.3781, -3.4360);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(britain, 5));

        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener(){


            public boolean onMarkerClick(final Marker marker) {

                Intent intent= getIntent();
                intent.putExtra("location", marker.getTitle());
                setResult(RESULT_OK, intent);
                finish();

                return false;
            }



        });
    }
}