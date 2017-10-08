package com.example.yx_jo.dublinbikesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddJourneyActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button  buttonAdd, picUpload, buttonchoose;
    private ImageView imageView;
    private DatabaseReference databaseJourneys;
    private double startLat, startLong, endLat, endLong;
    private LatLng startPosition, endPosition;
    private String startPointName, endPointName, downloadUrl;
    private TextView startName, endName;
    private StorageReference storageRef, imagesRef;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PICK_STATION_REQUEST = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);


        //auth
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //gettng references + initializing

        storageRef = FirebaseStorage.getInstance().getReference();

        //gets journeys associated with that user
        databaseJourneys = FirebaseDatabase.getInstance().getReference("journeys" + user.getUid());

        imageView = (ImageView) findViewById(R.id.imageViewJourney);

        startName = (TextView) findViewById(R.id.stationstartName);
        endName = (TextView) findViewById(R.id.stationendName);
        buttonAdd = (Button) findViewById(R.id.journeyAddButton);
        picUpload = (Button) findViewById(R.id.JourneyCamera);
        buttonchoose = (Button) findViewById(R.id.journeyGetButton);

        startName.setText(R.string.StartStation);
        endName.setText(R.string.FinishStation);

        buttonchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(AddJourneyActivity.this, MapsActivity.class), PICK_STATION_REQUEST);
            }
        });

        picUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addJourney();
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

    // Process clicks on Options Menu items to various activities
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



    private void addJourney(){


        if(!TextUtils.isEmpty(startPointName)){

            String id = databaseJourneys.push().getKey();

            if(!TextUtils.isEmpty(downloadUrl)){

                //see explanation of splitting latitude and longitude into discrete fields below
                Journey journey = new Journey(id, startPointName, startLat, startLong, endPointName, endLat, endLong, new Date(), downloadUrl);
                databaseJourneys.child(id).setValue(journey);
                Toast.makeText(this, "Journey added", Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                Journey journey = new Journey(id, startPointName, startLat, startLong, endPointName, endLat, endLong, new Date());
                databaseJourneys.child(id).setValue(journey);
                Toast.makeText(this, "Journey added", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else{
            Toast.makeText(this, "You must choose your stations", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_STATION_REQUEST && resultCode == RESULT_OK){


            //This is what's called when the maps activity data is received
            /*

            I have this bundling LatLng but getting doubles as FireBase doesn't like uploading
            and fetching LatLng so I have to split it. However for map-related activities it's as easy to
            keep them bundled together.
             */
            Bundle bundle = data.getParcelableExtra("bundle");
            startPosition = bundle.getParcelable("from_position");
            endPosition = bundle.getParcelable("to_position");
            startLat = startPosition.latitude;
            startLong = startPosition.longitude;
            endLat = endPosition.latitude;
            endLong = endPosition.longitude;
            startPointName = data.getStringExtra("startPointName");
            endPointName = data.getStringExtra("endPointName");
            startName.append(startPointName);
            endName.append(endPointName);

        }

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            //get the camera image
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();



            /*************** UPLOADS THE PIC TO FIREBASE***************/
            storageRef = FirebaseStorage.getInstance().getReference();



//name of the image file with date tag for unique entry

            imagesRef = storageRef.child("journey" + new Date().getTime());



//upload image

            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(),"Sending failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl =  taskSnapshot.getDownloadUrl().toString();

                    //attach downloaded image to activity and inform user
                    //USER CAN BYPASS THIS by taking photo and clicking add before it's uploaded

                    Picasso.with(AddJourneyActivity.this).load(downloadUrl.toString()).fit().centerCrop().into(imageView);



                    Toast.makeText(getApplicationContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}
