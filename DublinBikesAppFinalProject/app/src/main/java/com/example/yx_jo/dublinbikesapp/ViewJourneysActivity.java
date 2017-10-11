package com.example.yx_jo.dublinbikesapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewJourneysActivity extends AppCompatActivity {

    private DatabaseReference databaseJourneys;
    private ListView listViewJourneys;
    private FirebaseAuth firebaseAuth;



    List<Journey> journeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journeys);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        


        //gets journeys related to that user

        databaseJourneys = FirebaseDatabase.getInstance().getReference("journeys" + user.getUid());

        listViewJourneys = (ListView) findViewById(R.id.listViewJourneys);

        listViewJourneys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Journey journey = (Journey) listViewJourneys.getAdapter().getItem(i);



                //pretty messy intent, but it's due to me saving the LatLng as separate doubles,
                //then bundling them back up when using them to set markers on the map
                // I did this because FireBase couldn't read out LatLngs (but it could accept them).

                //Anyhow, this is fired when a listview item is clicked and it sends the user to the map

                Intent intent = new Intent(ViewJourneysActivity.this, HistoricalJourneyMapsActivity.class);
                LatLng startPos = new LatLng(journey.getStartLat(), journey.getStartLong());
                LatLng endPos = new LatLng(journey.getEndLat(), journey.getEndLong() );
                String startName = journey.getStartName();
                String endName = journey.getEndName();
                Bundle args = new Bundle();
                args.putParcelable("from_position", startPos);
                args.putParcelable("to_position", endPos);
                intent.putExtra("bundle", args);
                intent.putExtra("startPointName", startName);
                intent.putExtra("endPointName", endName);
                startActivity(intent);
            }
        });


        journeyList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseJourneys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //fetches journeys and adds them to adapter

                journeyList.clear();
                for(DataSnapshot journeySnapShot: dataSnapshot.getChildren()){
                    Journey journey = journeySnapShot.getValue(Journey.class);
                    journeyList.add(journey);
                }

                JourneyList adapter = new JourneyList(ViewJourneysActivity.this, journeyList);
                listViewJourneys.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}
