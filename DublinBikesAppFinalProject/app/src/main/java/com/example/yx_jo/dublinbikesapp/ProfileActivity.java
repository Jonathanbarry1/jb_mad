package com.example.yx_jo.dublinbikesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserDetails;
    private Button buttonLogout, buttonAddJourney, buttonViewJourney, buttonUpdateInfo;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewUserDetails = (TextView) findViewById(R.id.textViewUserDetail);
       String userDetails;

        //checks to see whether user has entered a name, and if so, displays it, if not displays email
        if(user.getDisplayName() != null){

            userDetails = getResources().getString(R.string.welcome) + " " + user.getDisplayName();
            textViewUserDetails.setText(userDetails);
        }
        else{
            userDetails  = getResources().getString(R.string.welcome) + " " + user.getEmail();
            textViewUserDetails.setText(userDetails);
        }

        buttonUpdateInfo = (Button) findViewById(R.id.buttonUpdateInfo);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonViewJourney = (Button) findViewById(R.id.buttonViewJourney);
        buttonAddJourney = (Button) findViewById(R.id.buttonAddJourney);
        buttonViewJourney.setOnClickListener(this);
        buttonAddJourney.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
        buttonUpdateInfo.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == buttonUpdateInfo){
            startActivity(new Intent(this, UpdateInfo.class));
        }

        if(view == buttonViewJourney){
            startActivity(new Intent(this, ViewJourneysActivity.class));
        }

        if(view == buttonAddJourney){
            startActivity(new Intent(this, AddJourneyActivity.class));
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
}
