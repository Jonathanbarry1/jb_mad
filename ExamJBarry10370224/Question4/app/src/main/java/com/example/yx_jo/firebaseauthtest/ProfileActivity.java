package com.example.yx_jo.firebaseauthtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;
    private Button button, button2;
    private EditText editTextHero, editTextVillain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        button = (Button)findViewById(R.id.buttonViewCharacters);
        button2 = (Button) findViewById(R.id.buttonLogOut);
        editTextHero = (EditText) findViewById(R.id.editTextHero);
        editTextVillain = (EditText) findViewById(R.id.editTextVillain);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view == button){
            String hero = editTextHero.getText().toString();
            String villain = editTextVillain.getText().toString();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Name", hero);
            editor.putString("Villain",villain);
            editor.apply();
            Toast.makeText(ProfileActivity.this, "Preferences saved", Toast.LENGTH_SHORT).show();
        }

        if(view == button2){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


    }
}
