package com.example.yx_jo.dublinbikesapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateInfo extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonSaveInfo;


    private DatabaseReference databaseReference;

    private EditText editTextName, editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSaveInfo = (Button) findViewById(R.id.buttonSaveInfo);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String userDetails;
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        if(user.getDisplayName() != null){
            userDetails = getResources().getString(R.string.welcome) + " " + user.getDisplayName();
            textViewUserEmail.setText(userDetails);
        }
        else{
            userDetails = getResources().getString(R.string.welcome) + " " + user.getEmail();
            textViewUserEmail.setText(userDetails);
        }

        buttonSaveInfo.setOnClickListener(this);

    }

    private void saveUserInformation(){
        String name = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name, add);

        FirebaseUser user = firebaseAuth.getCurrentUser();


        //saves data in database then adds it to user profile


        databaseReference.child(user.getUid()).setValue(userInformation);



        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(UpdateInfo.this, ProfileActivity.class));

                        }
                    }
                });





    }

    @Override
    public void onClick(View view) {

        if(view == buttonSaveInfo){
            saveUserInformation();
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